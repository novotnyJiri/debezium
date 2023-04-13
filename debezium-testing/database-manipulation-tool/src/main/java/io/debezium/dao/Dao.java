package io.debezium.dao;

import io.debezium.entity.DatabaseEntry;

import java.util.List;
import java.util.Optional;

public interface Dao {
    Optional<DatabaseEntry> get(long id);

    List<DatabaseEntry> getAll();

    void insert(DatabaseEntry databaseEntry);

    void delete(DatabaseEntry databaseEntry);

    void update(DatabaseEntry databaseEntry);

    void upsert(DatabaseEntry databaseEntry);

    void createTable(DatabaseEntry databaseEntry);

    void createTableAndInsert(DatabaseEntry databaseEntry);

}
