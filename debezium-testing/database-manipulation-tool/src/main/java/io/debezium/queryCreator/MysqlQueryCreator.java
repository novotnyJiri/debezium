/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.queryCreator;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class MysqlQueryCreator extends AbstractBasicQueryCreator {

    public MysqlQueryCreator() {
    }

    @Override
    public String resetDatabase(String schema) {
        String query = "DROP SCHEMA IF EXISTS" + schema + "; CREATE SCHEMA " + schema;
        LOG.debug("Created DATABASE RESET query: " + query);
        return query;
    }
}
