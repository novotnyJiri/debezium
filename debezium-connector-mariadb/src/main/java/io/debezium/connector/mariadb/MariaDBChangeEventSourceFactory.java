/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mariadb;

import java.util.Optional;
import java.util.function.Function;

import org.apache.kafka.connect.source.SourceRecord;

import io.debezium.connector.base.ChangeEventQueue;
import io.debezium.jdbc.MainConnectionProvidingConnectionFactory;
import io.debezium.pipeline.DataChangeEvent;
import io.debezium.pipeline.ErrorHandler;
import io.debezium.pipeline.EventDispatcher;
import io.debezium.pipeline.notification.NotificationService;
import io.debezium.pipeline.source.snapshot.incremental.IncrementalSnapshotChangeEventSource;
import io.debezium.pipeline.source.snapshot.incremental.SignalBasedIncrementalSnapshotChangeEventSource;
import io.debezium.pipeline.source.spi.ChangeEventSourceFactory;
import io.debezium.pipeline.source.spi.DataChangeEventListener;
import io.debezium.pipeline.source.spi.SnapshotChangeEventSource;
import io.debezium.pipeline.source.spi.SnapshotProgressListener;
import io.debezium.pipeline.source.spi.StreamingChangeEventSource;
import io.debezium.relational.TableId;
import io.debezium.spi.schema.DataCollectionId;
import io.debezium.util.Clock;
import io.debezium.util.Strings;

public class MariaDBChangeEventSourceFactory implements ChangeEventSourceFactory<MariaDBPartition, MariaDBOffsetContext> {

    private final MariaDBConnectorConfig configuration;
    private final MainConnectionProvidingConnectionFactory<MariaDBConnection> connectionFactory;
    private final ErrorHandler errorHandler;
    private final EventDispatcher<MariaDBPartition, TableId> dispatcher;
    private final Clock clock;
    private final MariaDBTaskContext taskContext;
    private final MariaDBStreamingChangeEventSourceMetrics streamingMetrics;
    private final MySqlDatabaseSchema schema;
    // MySQL snapshot requires buffering to modify the last record in the snapshot as sometimes it is
    // impossible to detect it till the snapshot is ended. Mainly when the last snapshotted table is empty.
    // Based on the DBZ-3113 the code can change in the future and it will be handled not in MySQL
    // but in the core shared code.
    private final ChangeEventQueue<DataChangeEvent> queue;

    public MariaDBChangeEventSourceFactory(MariaDBConnectorConfig configuration, MainConnectionProvidingConnectionFactory<MariaDBConnection> connectionFactory,
                                           ErrorHandler errorHandler, EventDispatcher<MariaDBPartition, TableId> dispatcher, Clock clock, MySqlDatabaseSchema schema,
                                           MariaDBTaskContext taskContext, MariaDBStreamingChangeEventSourceMetrics streamingMetrics,
                                           ChangeEventQueue<DataChangeEvent> queue) {
        this.configuration = configuration;
        this.connectionFactory = connectionFactory;
        this.errorHandler = errorHandler;
        this.dispatcher = dispatcher;
        this.clock = clock;
        this.taskContext = taskContext;
        this.streamingMetrics = streamingMetrics;
        this.queue = queue;
        this.schema = schema;
    }

    @Override
    public SnapshotChangeEventSource<MariaDBPartition, MariaDBOffsetContext> getSnapshotChangeEventSource(SnapshotProgressListener<MariaDBPartition> snapshotProgressListener) {
        return new MySqlSnapshotChangeEventSource(configuration, connectionFactory, taskContext.getSchema(), dispatcher, clock,
                (MySqlSnapshotChangeEventSourceMetrics) snapshotProgressListener, this::modifyAndFlushLastRecord);
    }

    private void modifyAndFlushLastRecord(Function<SourceRecord, SourceRecord> modify) throws InterruptedException {
        queue.flushBuffer(dataChange -> new DataChangeEvent(modify.apply(dataChange.getRecord())));
        queue.disableBuffering();
    }

    @Override
    public StreamingChangeEventSource<MariaDBPartition, MariaDBOffsetContext> getStreamingChangeEventSource() {
        queue.disableBuffering();
        return new MariaDBStreamingChangeEventSource(
                configuration,
                connectionFactory.mainConnection(),
                dispatcher,
                errorHandler,
                clock,
                taskContext,
                streamingMetrics);
    }

    @Override
    public Optional<IncrementalSnapshotChangeEventSource<MariaDBPartition, ? extends DataCollectionId>> getIncrementalSnapshotChangeEventSource(
                                                                                                                                              MariaDBOffsetContext offsetContext,
                                                                                                                                              SnapshotProgressListener<MariaDBPartition> snapshotProgressListener,
                                                                                                                                              DataChangeEventListener<MariaDBPartition> dataChangeEventListener,
                                                                                                                                              NotificationService<MariaDBPartition, MariaDBOffsetContext> notificationService) {

        if (configuration.isReadOnlyConnection()) {
            if (connectionFactory.mainConnection().isGtidModeEnabled()) {
                return Optional.of(new MySqlReadOnlyIncrementalSnapshotChangeEventSource<>(
                        configuration,
                        connectionFactory.mainConnection(),
                        dispatcher,
                        schema,
                        clock,
                        snapshotProgressListener,
                        dataChangeEventListener,
                        notificationService));
            }
            throw new UnsupportedOperationException("Read only connection requires GTID_MODE to be ON");
        }
        // If no data collection id is provided, don't return an instance as the implementation requires
        // that a signal data collection id be provided to work.
        if (Strings.isNullOrEmpty(configuration.getSignalingDataCollectionId())) {
            return Optional.empty();
        }
        return Optional.of(new SignalBasedIncrementalSnapshotChangeEventSource<>(
                configuration,
                connectionFactory.mainConnection(),
                dispatcher,
                schema,
                clock,
                snapshotProgressListener,
                dataChangeEventListener, notificationService));
    }
}
