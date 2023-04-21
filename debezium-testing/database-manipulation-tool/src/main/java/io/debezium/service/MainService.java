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
import io.debezium.entity.DatabaseEntry;

@ApplicationScoped
public class MainService {
    @Inject
    DaoManager daoManager;

    public void insert(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.insert(dbEntity);
        }
    }

    public void createTable(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.createTable(dbEntity);
        }
    }

    public void upsert(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.upsert(dbEntity);
        }
    }

    public void update(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.update(dbEntity);
        }
    }

    public void CreateTableAndUpsert(DatabaseEntry dbEntity) {
        for (Dao dao : daoManager.getEnabledDbs()) {
            dao.createTableAndUpsert(dbEntity);
        }
    }
}
