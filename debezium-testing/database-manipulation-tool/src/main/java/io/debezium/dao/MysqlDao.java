/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dao;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.debezium.exception.RuntimeSQLException;
import org.eclipse.microprofile.faulttolerance.Retry;

import io.debezium.dataSource.MysqlDataSource;
import io.debezium.queryCreator.MysqlQueryCreator;
import io.quarkus.arc.Unremovable;
import io.quarkus.arc.lookup.LookupIfProperty;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton
@LookupIfProperty(name = "quarkus.datasource.mysql.enabled", stringValue = "true")
@Unremovable
@Retry
public final class MysqlDao extends AbstractBasicDao {

    @Inject
    public MysqlDao(MysqlDataSource source, MysqlQueryCreator queryCreator) {
        super(source, queryCreator);
    }

    @Override
    public void resetDatabase() {
        try (Connection conn = source.getConnection();
             Statement stmt = conn.createStatement()) {
            String schema = conn.getCatalog();
            stmt.execute(queryCreator.resetDatabase(schema));
        }
        catch (SQLException ex) {
            LOG.error("Could not reset database");
            LOG.error(ex.getMessage());
            throw new RuntimeSQLException(ex);
        }
    }
}
