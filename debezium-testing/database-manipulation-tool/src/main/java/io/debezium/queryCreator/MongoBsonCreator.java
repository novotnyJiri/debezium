package io.debezium.queryCreator;

import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;

import com.mongodb.client.model.Updates;

import io.debezium.entity.DatabaseEntry;

@ApplicationScoped
public class MongoBsonCreator {
    private static final Logger LOG = Logger.getLogger(MongoBsonCreator.class);

    public Bson updateBson(DatabaseEntry databaseEntry) {
        return Updates.combine(
                databaseEntry.getColumnEntries().stream()
                        .map(columnEntry -> Updates.set(columnEntry.getColumnName(), columnEntry.getValue()))
                        .collect(Collectors.toList()));
    }

    public Document insertDocument(DatabaseEntry databaseEntry) {
        Document document = new Document();
        databaseEntry.getColumnEntries()
                .forEach(columnEntry -> document.put(columnEntry.getColumnName(), columnEntry.getValue()));
        return document;
    }
}
