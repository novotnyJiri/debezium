/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.model;

import java.util.*;

public class DatabaseTableMetadata {
    private String name;
    private List<DatabaseColumn> columns;

    public DatabaseTableMetadata(String name, List<DatabaseColumn> columns) {
        this.name = name;
        this.columns = columns;
    }

    public DatabaseTableMetadata() {
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

    @Override
    public String toString() {
        return "DatabaseTable{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseTableMetadata that = (DatabaseTableMetadata) o;
        return Objects.equals(name, that.name) && Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columns);
    }

    public boolean isSubsetOf(DatabaseTableMetadata b) {
        return new HashSet<>(b.columns).containsAll(columns);

    }
}
