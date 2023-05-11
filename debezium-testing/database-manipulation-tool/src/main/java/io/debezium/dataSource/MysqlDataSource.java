/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dataSource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;

@ApplicationScoped
public class MysqlDataSource implements DataSourceWrapper {
    @Inject
    @DataSource("mysql")
    AgroalDataSource source;

    @Override
    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
