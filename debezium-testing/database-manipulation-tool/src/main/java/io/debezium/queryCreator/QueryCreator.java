package io.debezium.queryCreator;

import io.debezium.entity.DatabaseEntry;
import io.debezium.entity.DatabaseTable;

public interface QueryCreator {
    String insertQuery(DatabaseEntry databaseEntry);

    String createTableQuery(DatabaseTable databaseTable);

    String upsertQuery(DatabaseEntry databaseEntry);

    String updateQuery(DatabaseEntry databaseEntry);
}
