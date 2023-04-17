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