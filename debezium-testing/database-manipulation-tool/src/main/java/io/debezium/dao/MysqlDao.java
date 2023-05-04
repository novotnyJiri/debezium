/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dao;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.microprofile.faulttolerance.Retry;

import io.debezium.dataSource.MysqlDataSource;
import io.debezium.model.DatabaseEntry;
import io.debezium.queryCreator.MysqlQueryCreator;
import io.quarkus.arc.Unremovable;
import io.quarkus.arc.lookup.LookupIfProperty;

@Singleton
@LookupIfProperty(name = "quarkus.datasource.mysql.enabled", stringValue = "true")
@Unremovable
@Retry
public class MysqlDao extends AbstractBasicDao {

    @Inject
    public MysqlDao(MysqlDataSource source, MysqlQueryCreator queryCreator) {
        super(source, queryCreator);
    }

    @Override
    public void delete(DatabaseEntry databaseEntry) {

    }
}
