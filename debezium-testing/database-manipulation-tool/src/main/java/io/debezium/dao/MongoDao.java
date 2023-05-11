/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.dao;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import io.debezium.model.DatabaseColumn;
import io.debezium.model.DatabaseColumnEntry;
import io.debezium.model.DatabaseEntry;
import io.debezium.model.DatabaseTableMetadata;
import io.debezium.queryCreator.MongoBsonCreator;
import io.quarkus.arc.Unremovable;
import io.quarkus.arc.lookup.LookupIfProperty;
import io.quarkus.mongodb.MongoClientName;

@ApplicationScoped
@LookupIfProperty(name = "quarkus.mongodb.main.enabled", stringValue = "true")
@Unremovable
@Retry
public class MongoDao implements Dao {

    @ConfigProperty(name = "mongodb.database")
    String databaseName;

    @Inject
    @MongoClientName("main")
    MongoClient client;

    @Inject
    MongoBsonCreator bsonCreator;

    private static final Logger LOG = Logger.getLogger(MongoDao.class);

    @Override
    public void insert(DatabaseEntry databaseEntry) {
        try {
            MongoDatabase db = getDatabase();
            Document insert = bsonCreator.insertDocument(databaseEntry);
            db.getCollection(databaseEntry.getDatabaseTableMetadata().getName()).insertOne(insert);
        }
        catch (Exception me) {
            LOG.error("Could not insert into database" + databaseEntry);
            LOG.error(me.getMessage());
            throw me;
        }
    }

    @Override
    public void delete(DatabaseEntry databaseEntry) {

    }

    @Override
    public void update(DatabaseEntry databaseEntry) {
        try {
            MongoDatabase db = getDatabase();
            Optional<DatabaseColumnEntry> primary = databaseEntry.getPrimaryColumnEntry();
            if (primary.isEmpty()) {
                throw new RuntimeException("Cannot update without primary key");
            }
            Bson filter = Filters.eq(primary.get().columnName(), primary.get().value());
            Bson update = bsonCreator.updateBson(databaseEntry);
            db.getCollection(databaseEntry.getDatabaseTableMetadata().getName()).updateOne(filter, update);
        }
        catch (Exception me) {
            LOG.error("Could not update database" + databaseEntry);
            LOG.error(me.getMessage());
            throw me;
        }
    }

    @Override
    public void upsert(DatabaseEntry databaseEntry) {
        try {
            MongoDatabase db = getDatabase();
            Optional<DatabaseColumnEntry> primary = databaseEntry.getPrimaryColumnEntry();
            if (primary.isEmpty()) {
                insert(databaseEntry);
                return;
            }
            Bson filter = Filters.eq(primary.get().columnName(), primary.get().value());
            Bson update = bsonCreator.updateBson(databaseEntry);
            UpdateOptions options = new UpdateOptions().upsert(true);
            db.getCollection(databaseEntry.getDatabaseTableMetadata().getName()).updateOne(filter, update, options);
            LOG.debug("Successful upsert " + databaseEntry);
        }
        catch (Exception me) {
            LOG.error("Could not upsert " + databaseEntry);
            LOG.error(me.getMessage());
            throw me;
        }
    }

    @Override
    public void createTable(DatabaseTableMetadata metadata) {
        try {
            MongoDatabase db = getDatabase();
            db.createCollection(metadata.getName());
        }
        catch (MongoException me) {
            LOG.error("Could not create table " + metadata);
            LOG.error(me.getMessage());
            throw me;
        }
    }

    /**
     * Since Mongo does table alteration automatically this method does not need to do anything. Due to implementing the DAO interface the method must be here even though is empty.
     * @param columns does nothing
     * @param metadata does nothing
     */
    @Override
    public void alterTable(List<DatabaseColumn> columns, DatabaseTableMetadata metadata) {
    }

    @Override
    public void createTableAndUpsert(DatabaseEntry databaseEntry) {
        upsert(databaseEntry);
    }

    private MongoDatabase getDatabase() {
        return client.getDatabase(databaseName);
    }
}
