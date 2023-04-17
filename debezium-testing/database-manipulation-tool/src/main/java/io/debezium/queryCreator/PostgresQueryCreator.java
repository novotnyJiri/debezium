package io.debezium.queryCreator;

import io.debezium.entity.DatabaseEntry;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostgresQueryCreator extends AbstractBasicQueryCreator {

    public PostgresQueryCreator() {}

    @Override
    public String UpsertQuery(DatabaseEntry databaseEntry) {
        return null;
    }
}
