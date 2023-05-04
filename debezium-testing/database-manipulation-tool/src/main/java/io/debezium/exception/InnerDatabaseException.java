package io.debezium.exception;

public class InnerDatabaseException extends RuntimeException {
    public InnerDatabaseException(String message) {
        super(message);
    }
}
