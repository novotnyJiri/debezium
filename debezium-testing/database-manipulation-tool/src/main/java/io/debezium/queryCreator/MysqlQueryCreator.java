package io.debezium.queryCreator;

import javax.enterprise.context.ApplicationScoped;

import io.debezium.entity.DatabaseColumnEntry;
import io.debezium.entity.DatabaseEntry;

@ApplicationScoped
public class MysqlQueryCreator extends AbstractBasicQueryCreator {

    public MysqlQueryCreator() {
    }

    @Override
    public String upsertQuery(DatabaseEntry databaseEntry) {
        StringBuilder builder = new StringBuilder(insertQuery(databaseEntry));
        builder.append(" ON DUPLICATE KEY UPDATE ");
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
}
