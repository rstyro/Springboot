package com.lrshuai.neo4j.multiple.db2;

import com.lrshuai.neo4j.multiple.config.DatabaseSelectionAwareNeo4jHealthIndicator;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Qualifier;
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


/**
 * neo4j 第二个数据源配置
 */
@Configuration(proxyBeanMethods = false)
@EnableNeo4jRepositories(
        basePackageClasses = Neo4jConfig2.class,
        neo4jMappingContextRef = "db2Context",
        neo4jTemplateRef = "db2Template",
        transactionManagerRef = "db2Manager"
)
public class Neo4jConfig2 {

    @Bean
    public Driver db2Driver(@Qualifier("db2Properties") Neo4jProperties neo4jProperties) {
        var authentication = neo4jProperties.getAuthentication();
        return GraphDatabase.driver(neo4jProperties.getUri(),
                AuthTokens.basic(authentication.getUsername(), authentication.getPassword()));
    }

    @Bean
    public Neo4jClient db2Client(@Qualifier("db2Driver") Driver driver, @Qualifier("db2Selection")DatabaseSelectionProvider databaseSelectionProvider) {
        return Neo4jClient.create(driver, databaseSelectionProvider);
    }

    @Bean
    public Neo4jOperations db2Template(@Qualifier("db2Client") Neo4jClient db2Client,@Qualifier("db2Context") Neo4jMappingContext neo4jMappingContext) {
        return new Neo4jTemplate(db2Client, neo4jMappingContext);
    }

    @Bean
    public DatabaseSelectionAwareNeo4jHealthIndicator db2HealthIndicator(@Qualifier("db2Driver") Driver driver,
                                                                           @Qualifier("db2Selection")DatabaseSelectionProvider db2Selection) {
        return new DatabaseSelectionAwareNeo4jHealthIndicator(driver, db2Selection);
    }

    @Bean
    public PlatformTransactionManager db2Manager(@Qualifier("db2Driver") Driver driver, @Qualifier("db2Selection") DatabaseSelectionProvider db2Selection) {
        return new Neo4jTransactionManager(driver, db2Selection);
    }

    @Bean
    public DatabaseSelectionProvider db2Selection(@Qualifier("db2DataProperties") Neo4jDataProperties dataProperties) {
        return () -> DatabaseSelection.byName(dataProperties.getDatabase());
    }

    @Bean
    public Neo4jMappingContext db2Context(ResourceLoader resourceLoader, Neo4jConversions neo4jConversions)
            throws ClassNotFoundException {
        Neo4jMappingContext context = new Neo4jMappingContext(neo4jConversions);
        context.setInitialEntitySet(Neo4jEntityScanner.get(resourceLoader).scan(this.getClass().getPackageName()));
        return context;
    }
}
