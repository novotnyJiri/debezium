/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.entity;

public class DatabaseColumnEntry {

    private final String value;
    private final String columnName;
    private final String dataType;

    public DatabaseColumnEntry(String value, String columnName, String dataType) {
        this.value = value;
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "DatabaseColumnEntry{" +
                "value='" + value + '\'' +
                ", columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
