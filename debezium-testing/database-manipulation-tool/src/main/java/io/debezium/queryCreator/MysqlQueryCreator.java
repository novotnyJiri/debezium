package io.debezium.queryCreator;

import io.debezium.entity.DatabaseColumnEntry;
import io.debezium.entity.DatabaseEntry;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MysqlQueryCreator extends AbstractBasicQueryCreator {

    public MysqlQueryCreator() {}

    @Override
    public String UpsertQuery(DatabaseEntry databaseEntry) {
        StringBuilder builder = new StringBuilder(InsertQuery(databaseEntry));
        builder.append(" ON DUPLICATE KEY UPDATE ");
        for (DatabaseColumnEntry entry : databaseEntry.getColumnEntries()) {
            builder.append(entry.getColumnName())
                    .append(" = ")
                    .append('\'')
                    .append(entry.getValue())
                    .append('\'')
                    .append(", ");
        }
        return builder.delete(builder.length() -2, builder.length()).toString();

    }
}
