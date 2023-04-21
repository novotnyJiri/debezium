/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dao;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;

import org.jboss.logging.Logger;

@ApplicationScoped
public class DaoManager {
    List<Dao> enabledDbs;

    private static final Logger LOG = Logger.getLogger(DaoManager.class);

    public DaoManager() {
        enabledDbs = new ArrayList<>();
        if (CDI.current().select(PostgresDao.class).isResolvable()) {
            enabledDbs.add(CDI.current().select(PostgresDao.class).get());
        }
        if (CDI.current().select(MysqlDao.class).isResolvable()) {
            enabledDbs.add(CDI.current().select(MysqlDao.class).get());
        }
        if (CDI.current().select(MongoDao.class).isResolvable()) {
            enabledDbs.add(CDI.current().select(MongoDao.class).get());
        }
    }

    public List<Dao> getEnabledDbs() {
        return enabledDbs;
    }
}
