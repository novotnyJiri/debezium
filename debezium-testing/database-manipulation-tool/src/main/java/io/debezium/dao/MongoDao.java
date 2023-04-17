package io.debezium.dao;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.conversions.Bson;
import org.jboss.logging.Logger;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import io.debezium.entity.DatabaseColumnEntry;
import io.debezium.entity.DatabaseEntry;
import io.debezium.queryCreator.MongoBsonCreator;
import io.quarkus.arc.Unremovable;
import io.quarkus.arc.lookup.LookupIfProperty;
import io.quarkus.mongodb.MongoClientName;

@ApplicationScoped
@LookupIfProperty(name = "quarkus.mongodb.main.enabled", stringValue = "true")
@Unremovable
public class MongoDao implements Dao {

    private static final String DATABASE_NAME = "quarkus_test";

    @Inject
    @MongoClientName("main")
    MongoClient client;

    @Inject
    MongoBsonCreator bsonCreator;

    private static final Logger LOG = Logger.getLogger(MongoDao.class);

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

    }

    @Override
    public void delete(DatabaseEntry databaseEntry) {

    }

    @Override
    public void update(DatabaseEntry databaseEntry) {

    }

    @Override
    public void upsert(DatabaseEntry databaseEntry) {
        try {
            MongoDatabase db = client.getDatabase(DATABASE_NAME);
            Optional<DatabaseColumnEntry> primary = databaseEntry.getPrimaryColumnEntry();
            if (primary.isEmpty()) {
                insert(databaseEntry);
                return;
            }
            Bson filter = Filters.eq(primary.get().getColumnName(), primary.get().getValue());
            Bson update = bsonCreator.updateBson(databaseEntry);
            UpdateOptions options = new UpdateOptions().upsert(true);
            db.getCollection(databaseEntry.getDatabaseTable().getName()).updateOne(filter, update, options);
        }
        catch (MongoException me) {
            LOG.error("Could not upsert " + databaseEntry);
            LOG.error(me);
        }
    }

    @Override
    public void createTable(DatabaseEntry databaseEntry) {
        try {
            MongoDatabase db = client.getDatabase(DATABASE_NAME);
            db.createCollection(databaseEntry.getDatabaseTable().getName());
        }
        catch (MongoException me) {
            LOG.error("Could not create table " + databaseEntry);
            LOG.error(me);
        }

    }

    @Override
    public void createTableAndInsert(DatabaseEntry databaseEntry) {

    }
}
