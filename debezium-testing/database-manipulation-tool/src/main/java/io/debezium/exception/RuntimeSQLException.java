package io.debezium.exception;

public class RuntimeSQLException extends RuntimeException {
    public RuntimeSQLException(Throwable cause) {
        super(cause);
    }
}
