/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.queryCreator;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.logging.Logger;

import io.debezium.entity.DatabaseColumn;
import io.debezium.entity.DatabaseColumnEntry;
import io.debezium.entity.DatabaseEntry;
import io.debezium.entity.DatabaseTable;

@ApplicationScoped
public class PostgresQueryCreator extends AbstractBasicQueryCreator {

    private static final Logger LOG = Logger.getLogger(PostgresQueryCreator.class);

    public PostgresQueryCreator() {
    }

    @Override
    public String upsertQuery(DatabaseEntry databaseEntry) {
        StringBuilder builder = new StringBuilder(insertQuery(databaseEntry));
        builder.append(" ON CONFLICT (")
                .append(databaseEntry.getPrimaryColumnEntry().get().getColumnName())
                .append(") DO UPDATE SET ");
        for (DatabaseColumnEntry entry : databaseEntry.getColumnEntries()) {
            builder.append(entry.getColumnName())
                    .append(" = ")
                    .append('\'')
                    .append(entry.getValue())
                    .append('\'')
                    .append(", ");
        }
        return builder.delete(builder.length() - 2, builder.length()).toString();
    }

    @Override
    public String createTableQuery(DatabaseTable databaseTable) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(databaseTable.getName())
                .append(" (");

        for (DatabaseColumn column : databaseTable.getColumns()) {
            builder.append(column.getName())
                    .append(" ")
                    .append(convertDouble(column.getDataType()))
                    .append(", ");
        }
        databaseTable.getPrimary().ifPresent(column -> builder.append("PRIMARY KEY (").append(column.getName()).append("), "));

        builder.delete(builder.length() - 2, builder.length())
                .append(")");
        String query = builder.toString();
        LOG.debug("CREATED TABLE CREATE QUERY: " + query);
        return query;
    }

    private String convertDouble(String dataType) {
        if (dataType.equalsIgnoreCase("double")) {
            return "Double Precision";
        }
        return dataType;
    }
}
