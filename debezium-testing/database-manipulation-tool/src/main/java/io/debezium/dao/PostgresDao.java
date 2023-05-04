/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dao;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.debezium.dataSource.PostgresDataSource;

import io.debezium.model.DatabaseEntry;
import io.debezium.queryCreator.PostgresQueryCreator;
import io.quarkus.arc.Unremovable;
import io.quarkus.arc.lookup.LookupIfProperty;
import org.eclipse.microprofile.faulttolerance.Retry;

@Singleton
@LookupIfProperty(name = "quarkus.datasource.postgresql.enabled", stringValue = "true")
@Unremovable
@Retry
public class PostgresDao extends AbstractBasicDao {

    @Inject
    public PostgresDao(PostgresDataSource source, PostgresQueryCreator queryCreator) {
        super(source, queryCreator);
    }

    @Override
    public void delete(DatabaseEntry databaseEntry) {

    }

}
