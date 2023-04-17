package io.debezium.queryCreator;

import io.debezium.entity.DatabaseEntry;
import io.debezium.entity.DatabaseTable;

public interface QueryCreator {
    String InsertQuery(DatabaseEntry databaseEntry);

    String CreateTableQuery(DatabaseTable databaseTable);

    String UpsertQuery(DatabaseEntry databaseEntry);
}
