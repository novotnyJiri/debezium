/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mariadb.metadata;

import io.debezium.config.Field;
import io.debezium.connector.mariadb.Module;
import io.debezium.connector.mariadb.MySqlConnector;
import io.debezium.connector.mariadb.MySqlConnectorConfig;
import io.debezium.metadata.ConnectorDescriptor;
import io.debezium.metadata.ConnectorMetadata;

public class MySqlConnectorMetadata implements ConnectorMetadata {

    @Override
    public ConnectorDescriptor getConnectorDescriptor() {
        return new ConnectorDescriptor("mysql", "Debezium MySQL Connector", MySqlConnector.class.getName(), Module.version());
    }

    @Override
    public Field.Set getConnectorFields() {
        return MySqlConnectorConfig.ALL_FIELDS;
    }
}
