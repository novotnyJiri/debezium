package io.debezium.queryCreator;

import org.jboss.logging.Logger;

import io.debezium.entity.DatabaseColumn;
import io.debezium.entity.DatabaseColumnEntry;
import io.debezium.entity.DatabaseEntry;
import io.debezium.entity.DatabaseTable;

public abstract class AbstractBasicQueryCreator implements QueryCreator {

    private static final Logger LOG = Logger.getLogger(AbstractBasicQueryCreator.class);

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
                    .append(entry.getValue())
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
    public String createTableQuery(DatabaseTable databaseTable) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(databaseTable.getName())
                .append(" (");

        for (DatabaseColumn column : databaseTable.getColumns()) {
            builder.append(column.getName())
                    .append(" ")
                    .append(column.getDataType())
                    .append(", ");
        }
        databaseTable.getPrimary().ifPresent(column -> builder.append("PRIMARY KEY (").append(column.getName()).append("), "));

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
            builder.append(columnEntry.getColumnName())
                    .append(" = '")
                    .append(columnEntry.getValue())
                    .append("', ");
        }
        builder.delete(builder.length() - 2, builder.length())
                .append(" WHERE ")
                .append(databaseEntry.getPrimaryColumnEntry().get().getColumnName())
                .append(" = '")
                .append(databaseEntry.getPrimaryColumnEntry().get().getValue())
                .append("'");
        LOG.debug("Update query created:" + builder);
        return builder.toString();
    }

    public abstract String upsertQuery(DatabaseEntry databaseEntry);
}
