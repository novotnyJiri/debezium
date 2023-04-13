package io.debezium.entity;

import java.util.ArrayList;
import java.util.List;

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
}
