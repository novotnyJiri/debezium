package io.debezium.queryCreator;

import javax.enterprise.context.ApplicationScoped;

import io.debezium.entity.DatabaseEntry;

@ApplicationScoped
public class PostgresQueryCreator extends AbstractBasicQueryCreator {

    public PostgresQueryCreator() {
    }

    @Override
    public String UpsertQuery(DatabaseEntry databaseEntry) {
        return null;
    }
}
