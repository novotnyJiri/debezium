/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.queryCreator;

import io.debezium.model.DatabaseEntry;
import io.debezium.model.DatabaseTableMetadata;

public interface QueryCreator {
    String insertQuery(DatabaseEntry databaseEntry);

    String createTableQuery(DatabaseTableMetadata databaseTableMetadata);

    String upsertQuery(DatabaseEntry databaseEntry);

    String updateQuery(DatabaseEntry databaseEntry);
}
