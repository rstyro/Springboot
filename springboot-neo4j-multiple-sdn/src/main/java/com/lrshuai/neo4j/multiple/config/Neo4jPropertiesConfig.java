package com.lrshuai.neo4j.multiple.config;

import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataProperties;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration(proxyBeanMethods = false)
public class Neo4jPropertiesConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.neo4j")
    public Neo4jProperties neo4jProperties() {
        return new Neo4jProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.data.neo4j")
    public Neo4jDataProperties neo4jDataProperties() {
        return new Neo4jDataProperties();
    }

    @Bean
    @ConfigurationProperties("db2.spring.neo4j")
    public Neo4jProperties db2Properties() {
        return new Neo4jProperties();
    }

    @Bean
    @ConfigurationProperties("db2.spring.data.neo4j")
    public Neo4jDataProperties db2DataProperties() {
        return new Neo4jDataProperties();
    }

}
