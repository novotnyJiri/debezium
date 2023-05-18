/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.queryCreator;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import io.debezium.model.DatabaseColumn;
import io.debezium.model.DatabaseColumnEntry;
import io.debezium.model.DatabaseEntry;
import io.debezium.model.DatabaseTableMetadata;

@ApplicationScoped
public class PostgresQueryCreator extends AbstractBasicQueryCreator {

    private static final Logger LOG = Logger.getLogger(PostgresQueryCreator.class);

    public PostgresQueryCreator() {
    }

    @Override
    public String createTableQuery(DatabaseTableMetadata databaseTableMetadata) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(databaseTableMetadata.getName())
                .append(" (");

        for (DatabaseColumn column : databaseTableMetadata.getColumns()) {
            builder.append(column.getName())
                    .append(" ")
                    .append(convertDouble(column.getDataType()))
                    .append(", ");
        }
        databaseTableMetadata.getPrimary().ifPresent(column -> builder.append("PRIMARY KEY (").append(column.getName()).append("), "));

        builder.delete(builder.length() - 2, builder.length())
                .append(")");
        String query = builder.toString();
        LOG.debug("CREATED TABLE CREATE QUERY: " + query);
        return query;
    }

    @Override
    public String resetDatabase(String schema) {
        String query = "DROP SCHEMA IF EXISTS " + schema +
                " CASCADE; CREATE SCHEMA " + schema;
        LOG.debug("Created DATABASE RESET query: " + query);
        return query;
    }

    private String convertDouble(String dataType) {
        if (dataType.equalsIgnoreCase("double")) {
            return "Double Precision";
        }
        return dataType;
    }
}
