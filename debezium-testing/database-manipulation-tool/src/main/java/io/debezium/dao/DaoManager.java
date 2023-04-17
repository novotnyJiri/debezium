package io.debezium.dao;

import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import java.util.ArrayList;
import java.util.List;

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
