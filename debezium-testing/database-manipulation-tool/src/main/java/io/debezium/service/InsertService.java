package io.debezium.service;

import io.debezium.dao.Dao;
import io.debezium.dao.DaoManager;
import io.debezium.entity.DatabaseEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class InsertService {
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

}
