package io.debezium.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.agroal.api.AgroalDataSource;
import io.debezium.entity.DatabaseEntry;
import io.debezium.queryCreator.MysqlQueryCreator;
import io.quarkus.agroal.DataSource;
import io.quarkus.arc.Unremovable;
import io.quarkus.arc.lookup.LookupIfProperty;

@ApplicationScoped
@LookupIfProperty(name = "quarkus.datasource.mysql.enabled", stringValue = "true")
@Unremovable
public class MysqlDao implements Dao {

    @Inject
    @DataSource("mysql")
    AgroalDataSource source;

    @Inject
    MysqlQueryCreator queryCreator;

    private static final Logger LOG = Logger.getLogger(MysqlDao.class);
    private static final String DB_NAME = "Mysql";

    @Override
    public Optional<DatabaseEntry> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<DatabaseEntry> getAll() {
        return null;
    }

    @Override
    public void insert(DatabaseEntry databaseEntry) {
        try (Connection conn = source.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.InsertQuery(databaseEntry));
        }
        catch (SQLException ex) {
            LOG.error("Could not insert into database " + databaseEntry);
            LOG.error(ex);
        }
    }

    @Override
    public void delete(DatabaseEntry databaseEntry) {

    }

    @Override
    public void update(DatabaseEntry databaseEntry) {

    }

    @Override
    public void upsert(DatabaseEntry databaseEntry) {
        try (Connection conn = source.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.UpsertQuery(databaseEntry));
        }
        catch (SQLException ex) {
            LOG.error("Could not upsert " + databaseEntry);
            LOG.error(ex);
        }
    }

    @Override
    public void createTable(DatabaseEntry databaseEntry) {
        try (Connection conn = source.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.CreateTableQuery(databaseEntry.getDatabaseTable()));
        }
        catch (SQLException ex) {
            LOG.error("Could not create table " + databaseEntry);
            LOG.error(ex);
        }
    }

    @Override
    public void createTableAndInsert(DatabaseEntry databaseEntry) {

    }

    @Override
    public void createTableAndUpsert(DatabaseEntry databaseEntry) {
        createTable(databaseEntry);
        upsert(databaseEntry);
    }
}
