/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.queryCreator;

import org.jboss.logging.Logger;

import io.debezium.model.DatabaseColumn;
import io.debezium.model.DatabaseColumnEntry;
import io.debezium.model.DatabaseEntry;
import io.debezium.model.DatabaseTableMetadata;

public abstract class AbstractBasicQueryCreator implements QueryCreator {

    private final Logger LOG = Logger.getLogger(getClass());

    @Override
    public String insertQuery(DatabaseEntry databaseEntry) {
        StringBuilder builder = new StringBuilder("INSERT INTO ")
                .append(databaseEntry.getDatabaseTable().getName())
                .append(" (");

        for (DatabaseColumn column : databaseEntry.getColumns()) {
            builder.append(column.getName())
                    .append(", ");
        }
        builder.delete(builder.length() - 2, builder.length())
                .append(") VALUES (");

        for (DatabaseColumnEntry entry : databaseEntry.getColumnEntries()) {
            builder.append('\'')
                    .append(entry.value())
                    .append('\'')
                    .append(", ");
        }
        builder.delete(builder.length() - 2, builder.length())
                .append(")");
        String query = builder.toString();
        LOG.debug("CREATED INSERT QUERY: " + query);
        return query;
    }

    @Override
    public String createTableQuery(DatabaseTableMetadata databaseTableMetadata) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(databaseTableMetadata.getName())
                .append(" (");

        for (DatabaseColumn column : databaseTableMetadata.getColumns()) {
            builder.append(column.getName())
                    .append(" ")
                    .append(column.getDataType())
                    .append(", ");
        }
        databaseTableMetadata.getPrimary().ifPresent(column -> builder.append("PRIMARY KEY (").append(column.getName()).append("), "));

        builder.delete(builder.length() - 2, builder.length())
                .append(")");
        String query = builder.toString();
        LOG.debug("CREATED TABLE CREATE QUERY: " + query);
        return query;
    }

    /**
     *
     * @param databaseEntry with primary attribute
     * @return Update query for databaseEntry
     */
    @Override
    public String updateQuery(DatabaseEntry databaseEntry) {
        StringBuilder builder = new StringBuilder("UPDATE ")
                .append(databaseEntry.getDatabaseTable().getName())
                .append(" SET ");
        for (DatabaseColumnEntry columnEntry : databaseEntry.getColumnEntries()) {
            builder.append(columnEntry.columnName())
                    .append(" = '")
                    .append(columnEntry.value())
                    .append("', ");
        }
        builder.delete(builder.length() - 2, builder.length())
                .append(" WHERE ")
                .append(databaseEntry.getPrimaryColumnEntry().get().columnName())
                .append(" = '")
                .append(databaseEntry.getPrimaryColumnEntry().get().value())
                .append("'");
        LOG.debug("Update query created:" + builder);
        return builder.toString();
    }

    public abstract String upsertQuery(DatabaseEntry databaseEntry);
}
