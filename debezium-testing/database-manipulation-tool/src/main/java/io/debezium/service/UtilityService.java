package io.debezium.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

import org.jboss.logging.Logger;

import io.debezium.dao.DaoManager;
import io.debezium.model.Database;
import io.debezium.utils.ResponseJsonBuilder;

@ApplicationScoped
public class UtilityService {

    @Inject
    Database database;

    @Inject
    DaoManager daoManager;

    private static final Logger LOG = Logger.getLogger(UtilityService.class);

    public JsonObject getAllEntries() {
        return new ResponseJsonBuilder(database)
                .addDatabases(daoManager.getEnabledDbsNames())
                .build();
    }
}
