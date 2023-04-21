/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseTable {
    String name;
    List<DatabaseColumn> columns;

    public DatabaseTable(String name, List<DatabaseColumn> columns) {
        this.name = name;
        this.columns = columns;
    }

    public DatabaseTable() {
        name = "UNDEFINED";
        columns = new ArrayList<>();
    }

    public List<DatabaseColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DatabaseColumn> columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addColumn(DatabaseColumn column) {
        columns.add(column);
    }

    public Optional<DatabaseColumn> getPrimary() {
        for (DatabaseColumn column : columns) {
            if (column.isPrimary()) {
                return Optional.of(column);
            }
        }
        return Optional.empty();
    }
}
