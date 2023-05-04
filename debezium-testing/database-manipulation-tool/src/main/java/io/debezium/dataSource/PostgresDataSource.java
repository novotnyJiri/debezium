package io.debezium.dataSource;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
public class PostgresDataSource implements DataSourceWrapper {

    @Inject
    @DataSource("postgresql")
    AgroalDataSource source;

    @Override
    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
