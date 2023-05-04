package io.debezium.dataSource;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceWrapper {
    Connection getConnection() throws SQLException;
}
