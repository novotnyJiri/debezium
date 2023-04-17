package io.debezium.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.jboss.logging.Logger;

public class DatabaseEntry {

    private List<DatabaseColumnEntry> columnEntries;
    private final DatabaseTable databaseTable;

    private static final Logger LOG = Logger.getLogger(DatabaseEntry.class);

    public DatabaseEntry(List<DatabaseColumnEntry> columnEntries, DatabaseTable databaseTable) {
        this.columnEntries = columnEntries;
        this.databaseTable = databaseTable;
    }

    public DatabaseEntry(JsonObject inputJsonObject) {
        try {
            List<DatabaseColumnEntry> entries = new ArrayList<>();
            DatabaseTable table = new DatabaseTable();

            table.setName(inputJsonObject.getString("table"));
            String primary = inputJsonObject.getString("primary");
            JsonArray payload = inputJsonObject.getJsonArray("payload");

            for (JsonValue rawEntry : payload) {
                JsonObject objectEntry = rawEntry.asJsonObject();
                DatabaseColumnEntry entry = new DatabaseColumnEntry(objectEntry.getString("value"), objectEntry.getString("name"), objectEntry.getString("dataType"));
                entries.add(entry);
                table.addColumn(new DatabaseColumn(entry.getColumnName(), entry.getDataType(), primary.equals(entry.getColumnName())));
            }
            this.columnEntries = entries;
            this.databaseTable = table;

        }
        catch (Exception ex) {
            LOG.error("Could not parse DatabaseEntity from Json object");
            LOG.error("Tha cause was: " + ex);
            throw new JsonException("Could not parse DatabaseEntity from Json object", ex.getCause());
        }
    }

    public List<DatabaseColumnEntry> getColumnEntries() {
        return columnEntries;
    }

    public void setColumnEntries(List<DatabaseColumnEntry> columnEntries) {
        this.columnEntries = columnEntries;
    }

    public void addColumnEntry(DatabaseColumnEntry columnEntry) {
        columnEntries.add(columnEntry);
    }

    public List<DatabaseColumn> getColumns() {
        return databaseTable.getColumns();
    }

    public DatabaseTable getDatabaseTable() {
        return databaseTable;
    }

    public Optional<DatabaseColumnEntry> getPrimaryColumnEntry() {
        Optional<DatabaseColumn> primaryColumn = databaseTable.getPrimary();
        if (primaryColumn.isEmpty()) {
            return Optional.empty();
        }
        for (DatabaseColumnEntry columnEntry : columnEntries) {
            if (columnEntry.getColumnName().equals(primaryColumn.get().getName())) {
                return Optional.of(columnEntry);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "DatabaseEntity{" +
                "columnEntries=" + columnEntries +
                ", databaseTable=" + databaseTable +
                '}';
    }
}
