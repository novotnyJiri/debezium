/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.debezium.dao.Dao;
import io.debezium.dao.DaoManager;
import io.debezium.model.Database;
import io.debezium.model.DatabaseEntry;
import io.debezium.exception.InnerDatabaseException;
import org.jboss.logging.Logger;

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
        } catch (InnerDatabaseException ex) {
            LOG.error("Error when inserting entry into inner database");
            LOG.error(ex.getMessage());
            return;
        }
        insertToDao(dbEntity);
    }

    public void createTable(DatabaseEntry dbEntity) {
            database.createTableIfNotExists(dbEntity.getDatabaseTable());
            for (Dao dao : daoManager.getEnabledDbs()) {
                dao.createTable(dbEntity);
            }
    }

    public void upsert(DatabaseEntry dbEntity) {
        boolean update;
        boolean tableExists;
        try {
            tableExists = database.tableExists(dbEntity.getDatabaseTable().getName());
            update = database.upsertEntry(dbEntity);
        } catch (InnerDatabaseException ex) {
            LOG.error("Error when upserting entry into inner database");
            LOG.error(ex.getMessage());
            return;
        }
        if (!tableExists) {
            createTable(dbEntity);
        }
        if (update) {
            updateToDao(dbEntity);
        } else {
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
}
