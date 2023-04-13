package io.debezium.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.mutiny.sqlclient.Row;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestModel {
    private final long id;
    private final String name;

    public TestModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TestModel from(Row row) {
        return new TestModel(row.getLong("id"), row.getString("name"));
    }

    public static TestModel from(ResultSet row) throws SQLException {
        return new TestModel(row.getLong("id"), row.getString("name"));
    }
    @JsonProperty("id")
    public long getId() {
        return id;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }
}
