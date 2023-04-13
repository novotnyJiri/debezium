package io.debezium.queryCreator;

import io.debezium.entity.DatabaseColumn;
import io.debezium.entity.DatabaseColumnEntry;
import io.debezium.entity.DatabaseEntry;
import io.debezium.entity.DatabaseTable;
import org.jboss.logging.Logger;

public abstract class AbstractBasicQueryCreator implements QueryCreator {

    private static final Logger LOG = Logger.getLogger(AbstractBasicQueryCreator.class);

    @Override
    public String InsertQuery(DatabaseEntry databaseEntry) {
        StringBuilder builder = new StringBuilder("INSERT INTO ")
                .append(databaseEntry.getDatabaseTable().getName())
                .append(" (");

        for (DatabaseColumn column: databaseEntry.getColumns()) {
            builder.append(column.getName());
            builder.append(", ");
        }
        builder.delete(builder.length() -2, builder.length())
                .append(") VALUES (");

        for (DatabaseColumnEntry entry: databaseEntry.getColumnEntries()) {
            builder.append('\'');
            builder.append(entry.getValue());
            builder.append('\'');
            builder.append(", ");
        }
        builder.delete(builder.length() -2, builder.length())
                .append(")");
        String query = builder.toString();
        LOG.debug("CREATED INSERT QUERY: " + query);
        return query;
    }

    @Override
    public String CreateTableQuery(DatabaseTable databaseTable) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(databaseTable.getName())
                .append(" (id SERIAL PRIMARY KEY, ");

        for (DatabaseColumn column : databaseTable.getColumns()) {
            builder.append(column.getName())
                    .append(" ")
                    .append(column.getDataType())
                    .append(", ");
        }
        builder.delete(builder.length() -2, builder.length())
                .append(")");
        String query = builder.toString();
        LOG.debug("CREATED TABLE CREATE QUERY: " + query);
        LOG.info("CREATED TABLE CREATE QUERY: " + query);
        return query;
    }
}
