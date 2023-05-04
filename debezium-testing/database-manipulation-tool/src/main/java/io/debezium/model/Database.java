package io.debezium.model;

import io.debezium.exception.InnerDatabaseException;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Database {
    Map<String, DatabaseTable> tables;

    private static final Logger LOG = Logger.getLogger(Database.class);


    public Database() {
        tables = new HashMap<>();
    }

    public boolean tableExists (String name) {
        return tables.containsKey(name);
    }

    public DatabaseTable getTable (String name) {
        return tables.get(name);
    }

    public boolean entryExists (DatabaseEntry entry) {
        String tableName = entry.getDatabaseTable().getName();
        return tableExists(tableName) && getTable(tableName).rowExists(entry);
    }

public void createTableIfNotExists(DatabaseTableMetadata tableMetadata) {
        if (tableExists(tableMetadata.getName())) {
            return;
        }
        LOG.debug("Creating table in inner database");
        tables.put(tableMetadata.getName(), new DatabaseTable(tableMetadata));
    }

    //Not Working
//    public void updateTable(DatabaseTableMetadata tableMetadata) {
//        if (!tableExists(tableMetadata.getName())) {
//            throw new DatabaseException("Cannot update database if it does not exist");
//        }
//        tables.replace(tableMetadata.getName(), new DatabaseTable(tableMetadata));
//    }

    public void dropTable(String name) {
        tables.remove(name);
    }

    /**
     * @param entry
     * @return true if updated, false if inserted
     */
    public boolean upsertEntry (DatabaseEntry entry) {
        String tableName = entry.getDatabaseTable().getName();
        createTableIfNotExists(entry.getDatabaseTable());
        LOG.debug("Initiating table check" );
        if (!entry.getDatabaseTable().isSubsetOf(getTable(tableName).getMetadata())) {
            throw new InnerDatabaseException("Entry contains columns the database table does not");
        }
        //TODO: HERE WILL THE ALTER TABLE BE
        LOG.debug("finished checking table" );
        return getTable(tableName).putRow(entry);
    }

    public void insertEntry (DatabaseEntry entry) {
        String tableName = entry.getDatabaseTable().getName();
        createTableIfNotExists(entry.getDatabaseTable());
        if (entryExists(entry)) {
            throw new InnerDatabaseException("Table already contains entry");
        }
        if (!entry.getDatabaseTable().isSubsetOf(getTable(tableName).getMetadata())) {
            throw new InnerDatabaseException("Entry contains columns the database does not");
        }
        getTable(tableName).putRow(entry);
    }

    public void deleteEntry (DatabaseEntry entry) {
        DatabaseTable table = tables.get(entry.getDatabaseTable().getName());
        if (table == null) {
            LOG.debug("Table does not exist -> not deleting entry " + entry);
            return;
        }
        table.deleteRow(entry);
    }

    public List<DatabaseTable> getTables() {
        return  new ArrayList<>(tables.values());
    }

}
