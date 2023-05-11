package io.debezium.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import io.debezium.model.DatabaseColumn;
import io.debezium.model.DatabaseTableMetadata;
import org.jboss.logging.Logger;

import io.debezium.dataSource.DataSourceWrapper;
import io.debezium.exception.RuntimeSQLException;
import io.debezium.model.DatabaseEntry;
import io.debezium.queryCreator.QueryCreator;

public abstract class AbstractBasicDao implements Dao {

    protected DataSourceWrapper source;
    protected QueryCreator queryCreator;

    protected final Logger LOG = Logger.getLogger(getClass());

    public AbstractBasicDao(DataSourceWrapper source, QueryCreator queryCreator) {
        this.source = source;
        this.queryCreator = queryCreator;
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
    public void createTable(DatabaseTableMetadata metadata) {
        try (Connection conn = source.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.createTableQuery(metadata));
        }
        catch (SQLException ex) {
            LOG.error("Could not create table " + metadata);
            LOG.error(ex);
            throw new RuntimeSQLException(ex);
        }
    }

    @Override
    public void alterTable(List<DatabaseColumn> columns, DatabaseTableMetadata metadata) {
        try (Connection conn = source.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(queryCreator.addColumnsQuery(columns, metadata.getName()));
        }
        catch (SQLException ex) {
            LOG.error("Could not add columns " + columns + " to table " + metadata.getName());
            LOG.error(ex);
            throw new RuntimeSQLException(ex);
        }
    }
}
