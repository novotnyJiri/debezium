package io.debezium.dao;

import io.debezium.dataSource.DataSourceWrapper;
import io.debezium.exception.RuntimeSQLException;
import io.debezium.model.DatabaseEntry;
import io.debezium.queryCreator.QueryCreator;
import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBasicDao implements Dao {

    protected DataSourceWrapper source;
    protected QueryCreator queryCreator;

    protected final Logger LOG = Logger.getLogger(getClass());

    public AbstractBasicDao(DataSourceWrapper source, QueryCreator queryCreator) {
        this.source = source;
        this.queryCreator = queryCreator;
    }

    @Override
    public List<DatabaseEntry> getAll() {
        return null;
    }

    @Override
    public void insert(DatabaseEntry databaseEntry) {
        try (Connection conn = source.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.insertQuery(databaseEntry));
        }
        catch (SQLException ex) {
            LOG.error("Could not insert into database " + databaseEntry);
            LOG.error(ex);
            throw new RuntimeSQLException(ex);
        }
    }

    @Override
    public abstract void delete(DatabaseEntry databaseEntry);

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void update(DatabaseEntry databaseEntry) {
        try (Connection conn = source.getConnection();
             Statement stmt = conn.createStatement()) {
            if (databaseEntry.getPrimaryColumnEntry().isEmpty()) {
                throw new RuntimeException("Cannot update without primary key");
            }
            stmt.execute(queryCreator.updateQuery(databaseEntry));
        }
        catch (Exception ex) {
            LOG.error("Could not update database " + databaseEntry);
            LOG.error(ex);
            throw new RuntimeSQLException(ex);
        }
    }

    @Override
    public void upsert(DatabaseEntry databaseEntry) {
        try (Connection conn = source.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.upsertQuery(databaseEntry));
            LOG.debug("Successful upsert " + databaseEntry);
        }
        catch (SQLException ex) {
            LOG.error("Could not upsert " + databaseEntry);
            LOG.error(ex);
            throw new RuntimeSQLException(ex);
        }
    }

    @Override
    public void createTable(DatabaseEntry databaseEntry) {
        try (Connection conn = source.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.createTableQuery(databaseEntry.getDatabaseTable()));
        }
        catch (SQLException ex) {
            LOG.error("Could not create table " + databaseEntry);
            LOG.error(ex);
            throw new RuntimeSQLException(ex);
        }
    }

    @Override
    public void createTableAndUpsert(DatabaseEntry databaseEntry) {
        createTable(databaseEntry);
        upsert(databaseEntry);
    }
}
