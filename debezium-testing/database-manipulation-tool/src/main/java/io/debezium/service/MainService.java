/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.debezium.dao.Dao;
import io.debezium.dao.DaoManager;
import io.debezium.exception.InnerDatabaseException;
import io.debezium.model.Database;
import io.debezium.model.DatabaseColumn;
import io.debezium.model.DatabaseEntry;
import io.debezium.model.DatabaseTableMetadata;

@ApplicationScoped
public class MainService {
    @Inject
    DaoManager daoManager;

    @Inject
    Database database;

    private static final Logger LOG = Logger.getLogger(MainService.class);

    public void insert(DatabaseEntry dbEntity) {
        try {
            database.insertEntry(dbEntity);
        }
        catch (InnerDatabaseException ex) {
            LOG.error("Error when inserting entry into inner database");
            LOG.error(ex.getMessage());
            return;
        }
        insertToDao(dbEntity);
    }

    public void createTable(DatabaseEntry dbEntity) {
        List<DatabaseColumn> changedColumns = database.createOrAlterTable(dbEntity.getDatabaseTableMetadata());
        if (changedColumns == null) {
            LOG.debug("Creating table in Dbs " + dbEntity.getDatabaseTableMetadata());
            createTableToDao(dbEntity);
            return;
        }

        if (!changedColumns.isEmpty()) {
            LOG.debug("Altering table in Dbs " + dbEntity.getDatabaseTableMetadata());
            alterTableToDao(changedColumns, dbEntity.getDatabaseTableMetadata());
        }
    }

    public void upsert(DatabaseEntry dbEntity) {
        boolean update;
        try {
            createTable(dbEntity);
            update = database.upsertEntry(dbEntity);
        }
        catch (InnerDatabaseException ex) {
            LOG.error("Error when upserting entry into inner database");
            LOG.error(ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            LOG.error("Error when upserting entry into databases");
            LOG.error(ex.getMessage());
            throw ex;
        }
        if (update) {
            updateToDao(dbEntity);
        }
        else {
            insertToDao(dbEntity);
        }
    }

    public void update(DatabaseEntry dbEntity) {
        updateToDao(dbEntity);
    }

    private void insertToDao(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.insert(dbEntity);
        }
    }

    private void updateToDao(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.update(dbEntity);
        }
    }

    private void createTableToDao(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.createTable(dbEntity.getDatabaseTableMetadata());
        }
    }

    private void alterTableToDao(List<DatabaseColumn> columns, DatabaseTableMetadata metadata) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.alterTable(columns, metadata);
        }
    }

}
