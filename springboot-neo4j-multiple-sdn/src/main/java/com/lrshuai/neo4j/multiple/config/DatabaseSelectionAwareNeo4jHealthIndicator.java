package com.lrshuai.neo4j.multiple.config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.summary.DatabaseInfo;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.summary.ServerInfo;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.neo4j.core.DatabaseSelection;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class DatabaseSelectionAwareNeo4jHealthIndicator extends AbstractHealthIndicator {

    private final Driver driver;

    private final DatabaseSelectionProvider databaseSelectionProvider;

    public DatabaseSelectionAwareNeo4jHealthIndicator(Driver driver, DatabaseSelectionProvider databaseSelectionProvider) {
        this.driver = driver;
        this.databaseSelectionProvider = databaseSelectionProvider;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            SessionConfig sessionConfig = Optional.ofNullable(databaseSelectionProvider.getDatabaseSelection())
                    .filter(databaseSelection -> databaseSelection != DatabaseSelection.undecided())
                    .map(databaseSelection -> SessionConfig.builder().withDatabase(databaseSelection.getValue()).build())
                    .orElseGet(SessionConfig::defaultConfig);
            class Tuple {
                String edition;
                ResultSummary resultSummary;

                Tuple(String edition, ResultSummary resultSummary) {
                    this.edition = edition;
                    this.resultSummary = resultSummary;
                }
            }
            Tuple health = driver.session(sessionConfig)
                    .writeTransaction(tx -> {
                        Result result = tx
                                .run("CALL dbms.components() YIELD name, edition WHERE name = 'Neo4j Kernel' RETURN edition");
                        String edition = result.single().get("edition").asString();
                        return new Tuple(edition, result.consume());
                    });

            addHealthDetails(builder, health.edition, health.resultSummary);
        } catch (Exception ex) {
            builder.down().withException(ex);
        }
    }

    static void addHealthDetails(Health.Builder builder, String edition, ResultSummary resultSummary) {
        ServerInfo serverInfo = resultSummary.server();
        builder.up().withDetail("server", serverInfo.version() + "@" + serverInfo.address()).withDetail("edition",
                edition);
        DatabaseInfo databaseInfo = resultSummary.database();
        if (StringUtils.hasText(databaseInfo.name())) {
            builder.withDetail("database", databaseInfo.name());
        }
    }
}
