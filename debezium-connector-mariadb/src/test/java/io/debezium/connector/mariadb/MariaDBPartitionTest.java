/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mariadb;

import io.debezium.connector.common.AbstractPartitionTest;

public class MariaDBPartitionTest extends AbstractPartitionTest<MariaDBPartition> {

    @Override
    protected MariaDBPartition createPartition1() {
        return new MariaDBPartition("server1", "database1");
    }

    @Override
    protected MariaDBPartition createPartition2() {
        return new MariaDBPartition("server2", "database1");
    }
}
