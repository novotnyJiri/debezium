package io.debezium.dao;

import io.agroal.api.AgroalDataSource;
import io.debezium.entity.DatabaseEntry;
import io.debezium.queryCreator.MysqlQueryCreator;
import io.quarkus.agroal.DataSource;
import io.quarkus.arc.Unremovable;
import io.quarkus.arc.lookup.LookupIfProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

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
        try(Connection conn = source.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.InsertQuery(databaseEntry));
        } catch (SQLException ex) {
            LOG.error("Could not insert into database " + databaseEntry);
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

    }

    @Override
    public void createTable(DatabaseEntry databaseEntry) {
        try(Connection conn = source.getConnection();
            Statement stmt = conn.createStatement()) {
            LOG.info("Created create table statement");
            stmt.execute(queryCreator.CreateTableQuery(databaseEntry.getDatabaseTable()));
            LOG.info("Executed create table statement");
        } catch (SQLException ex) {
            LOG.error("Could not create table " + databaseEntry);
        }
    }

    @Override
    public void createTableAndInsert(DatabaseEntry databaseEntry) {

    }
}
