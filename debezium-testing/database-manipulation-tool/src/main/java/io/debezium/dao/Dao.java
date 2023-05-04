/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dao;

import java.util.List;
import java.util.Optional;

import io.debezium.model.DatabaseEntry;

public interface Dao {

    List<DatabaseEntry> getAll();

    void insert(DatabaseEntry databaseEntry);

    void delete(DatabaseEntry databaseEntry);

    void update(DatabaseEntry databaseEntry);

    @Deprecated
    void upsert(DatabaseEntry databaseEntry);

    void createTable(DatabaseEntry databaseEntry);

    void createTableAndUpsert(DatabaseEntry databaseEntry);

}
