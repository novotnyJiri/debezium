/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.queryCreator;

import io.debezium.entity.DatabaseEntry;
import io.debezium.entity.DatabaseTable;

public interface QueryCreator {
    String insertQuery(DatabaseEntry databaseEntry);

    String createTableQuery(DatabaseTable databaseTable);

    String upsertQuery(DatabaseEntry databaseEntry);

    String updateQuery(DatabaseEntry databaseEntry);
}
