package com.lrshuai.neo4j.multiple.db1;

import com.lrshuai.neo4j.multiple.config.DatabaseSelectionAwareNeo4jHealthIndicator;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataProperties;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.neo4j.config.Neo4jEntityScanner;
import org.springframework.data.neo4j.core.*;
import org.springframework.data.neo4j.core.convert.Neo4jConversions;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration(proxyBeanMethods = false)
@EnableNeo4jRepositories(
        basePackageClasses = Neo4jConfig1.class
        ,neo4jMappingContextRef = "db1Context",
        neo4jTemplateRef = "db1Template",
        transactionManagerRef = "db1TransactionManager"
)
public class Neo4jConfig1 {
    @Primary
    @Bean
    public Driver db1Driver(Neo4jProperties neo4jProperties) {
        var authentication = neo4jProperties.getAuthentication();
        return GraphDatabase.driver(neo4jProperties.getUri(),
                AuthTokens.basic(authentication.getUsername(), authentication.getPassword()));
    }

    @Primary
    @Bean
    public Neo4jClient neo4jClient(Driver db1Driver, DatabaseSelectionProvider db1SelectionProvider) {
        return Neo4jClient.create(db1Driver, db1SelectionProvider);
    }

    @Primary
    @Bean
    public Neo4jOperations db1Template(Neo4jClient neo4jClient,Neo4jMappingContext neo4jMappingContext) {
        return new Neo4jTemplate(neo4jClient, neo4jMappingContext);
    }

    @Primary
    @Bean
    public DatabaseSelectionAwareNeo4jHealthIndicator db1SelectionAwareNeo4jHealthIndicator(Driver db1Driver,
                                                                           DatabaseSelectionProvider db1SelectionProvider) {
        return new DatabaseSelectionAwareNeo4jHealthIndicator(db1Driver, db1SelectionProvider);
    }

    @Primary
    @Bean
    public PlatformTransactionManager db1TransactionManager(Driver db1Driver, DatabaseSelectionProvider db1SelectionProvider) {
        return new Neo4jTransactionManager(db1Driver, db1SelectionProvider);
    }

    @Primary
    @Bean
    public DatabaseSelectionProvider db1SelectionProvider(Neo4jDataProperties neo4jDataProperties) {
        return () -> DatabaseSelection.byName(neo4jDataProperties.getDatabase());
    }

    @Primary
    @Bean
    public Neo4jMappingContext db1Context(ResourceLoader resourceLoader, Neo4jConversions neo4jConversions)
            throws ClassNotFoundException {
        Neo4jMappingContext context = new Neo4jMappingContext(neo4jConversions);
        context.setInitialEntitySet(Neo4jEntityScanner.get(resourceLoader).scan(this.getClass().getPackageName()));
        return context;
    }
}
