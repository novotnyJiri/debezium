/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dao;

import io.debezium.model.DatabaseEntry;
import io.debezium.model.DatabaseTableMetadata;

public interface Dao {

    void insert(DatabaseEntry databaseEntry);

    void delete(DatabaseEntry databaseEntry);

    void update(DatabaseEntry databaseEntry);

    @Deprecated
    void upsert(DatabaseEntry databaseEntry);

    void createTable(DatabaseTableMetadata metadata);

    void alterTable(DatabaseTableMetadata current, DatabaseTableMetadata target);

    void createTableAndUpsert(DatabaseEntry databaseEntry);

}
