package io.debezium.entity;

import org.jboss.logging.Logger;

import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseEntry {

    private List<DatabaseColumnEntry> columnEntries;
    private DatabaseTable databaseTable;

    private static final Logger LOG = Logger.getLogger(DatabaseEntry.class);

    public DatabaseEntry(List<DatabaseColumnEntry> columnEntries, DatabaseTable databaseTable) {
        this.columnEntries = columnEntries;
        this.databaseTable = databaseTable;
    }

    public DatabaseEntry(JsonObject inputJsonObject) {
        try {
            List<DatabaseColumnEntry> entries = new ArrayList<>();
            DatabaseTable table = new DatabaseTable();

            JsonObject payload = inputJsonObject.getJsonObject("payload");
            table.setName(payload.getString("table"));

            for (JsonValue rawEntry: payload.getJsonArray("entries")) {
                JsonObject objectEntry = rawEntry.asJsonObject();
                DatabaseColumnEntry entry = new DatabaseColumnEntry(objectEntry.getString("value"), objectEntry.getString("name"), objectEntry.getString("dataType"));
                entries.add(entry);
                table.addColumn(new DatabaseColumn(entry.getColumnName(), entry.getDataType()));
            }
            this.columnEntries = entries;
            this.databaseTable = table;

        } catch (Exception ex) {
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
        return columnEntries.stream()
                .map(columnEntry -> new DatabaseColumn(columnEntry.getColumnName(), columnEntry.getDataType()))
                .collect(Collectors.toList());
    }

    public DatabaseTable getDatabaseTable() {
        return databaseTable;
    }

    @Override
    public String toString() {
        return "DatabaseEntity{" +
                "columnEntries=" + columnEntries +
                ", databaseTable=" + databaseTable +
                '}';
    }
}
