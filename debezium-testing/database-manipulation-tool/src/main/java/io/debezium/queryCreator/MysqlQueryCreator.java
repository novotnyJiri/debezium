/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.queryCreator;

import javax.enterprise.context.ApplicationScoped;

import io.debezium.model.DatabaseColumnEntry;
import io.debezium.model.DatabaseEntry;

@ApplicationScoped
public class MysqlQueryCreator extends AbstractBasicQueryCreator {

    public MysqlQueryCreator() {
    }
}
