/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.entity;

public class DatabaseColumn {
    String name;
    String dataType;
    boolean primary;

    public DatabaseColumn(String name, String dataType, boolean primary) {
        this.name = name;
        this.dataType = dataType;
        this.primary = primary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isPrimary() {
        return primary;
    }
}