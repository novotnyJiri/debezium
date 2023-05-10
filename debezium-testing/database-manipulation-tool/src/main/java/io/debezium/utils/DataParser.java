package io.debezium.utils;

import javax.json.JsonException;
import javax.json.JsonObject;

public interface DataParser<T> {
    T parse(JsonObject inputJsonObject) throws JsonException;
}
