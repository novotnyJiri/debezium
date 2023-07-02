/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mariadb;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.connect.connector.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.annotation.Immutable;
import io.debezium.config.Configuration;
import io.debezium.connector.common.RelationalBaseSourceConnector;
import io.debezium.relational.RelationalDatabaseConnectorConfig;

/**
 * A Kafka Connect source connector that creates tasks that read the MariaDB binary log and generate the corresponding
 * data change events.
 * <h2>Configuration</h2>
 * <p>
 * This connector is configured with the set of properties described in {@link MariaDBConnectorConfig}.
 *
 *
 * @author Randall Hauch
 */
public class MariaDBConnector extends RelationalBaseSourceConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MariaDBConnector.class);

    @Immutable
    private Map<String, String> properties;

    public MariaDBConnector() {
    }

    @Override
    public String version() {
        return Module.version();
    }

    @Override
    public void start(Map<String, String> props) {
        this.properties = Collections.unmodifiableMap(new HashMap<>(props));
    }

    @Override
    public Class<? extends Task> taskClass() {
        return MariaDBConnectorTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        if (maxTasks > 1) {
            throw new IllegalArgumentException("Only a single connector task may be started");
        }

        return Collections.singletonList(properties);
    }

    @Override
    public void stop() {
    }

    @Override
    public ConfigDef config() {
        return MariaDBConnectorConfig.configDef();
    }

    @Override
    protected void validateConnection(Map<String, ConfigValue> configValues, Configuration config) {
        ConfigValue hostnameValue = configValues.get(RelationalDatabaseConnectorConfig.HOSTNAME.name());
        // Try to connect to the database ...
        final MariaDBConnection.MySqlConnectionConfiguration connectionConfig = new MariaDBConnection.MySqlConnectionConfiguration(config);
        try (MariaDBConnection connection = new MariaDBConnection(connectionConfig)) {
            try {
                connection.connect();
                connection.execute("SELECT version()");
                LOGGER.info("Successfully tested connection for {} with user '{}'", connection.connectionString(), connectionConfig.username());
            }
            catch (SQLException e) {
                LOGGER.error("Failed testing connection for {} with user '{}'", connection.connectionString(), connectionConfig.username(), e);
                hostnameValue.addErrorMessage("Unable to connect: " + e.getMessage());
            }
        }
        catch (SQLException e) {
            LOGGER.error("Unexpected error shutting down the database connection", e);
        }
    }

    @Override
    protected Map<String, ConfigValue> validateAllFields(Configuration config) {
        return config.validate(MariaDBConnectorConfig.ALL_FIELDS);
    }
}
