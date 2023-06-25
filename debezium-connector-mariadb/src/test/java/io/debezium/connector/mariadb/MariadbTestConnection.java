/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.mariadb;

import static io.debezium.config.CommonConnectorConfig.DATABASE_CONFIG_PREFIX;
import static io.debezium.config.CommonConnectorConfig.DRIVER_CONFIG_PREFIX;

import java.sql.SQLException;
import java.util.Map;

import io.debezium.config.Configuration;
import io.debezium.jdbc.JdbcConfiguration;
import io.debezium.jdbc.JdbcConnection;

/**
 * A utility for integration test cases to connect the MySQL server running in the Docker container created by this module's
 * build.
 *
 * @author Randall Hauch
 */
public class MariadbTestConnection extends JdbcConnection {

    public enum MariaDbVersion {
        MARIADB_10_3,
    }

    private DatabaseDifferences databaseAsserts;
    private MariaDbVersion mariaDbVersion;

    /**
     * Obtain a connection instance to the named test database.
     *
     * @param databaseName the name of the test database
     * @return the MySQLConnection instance; never null
     */
    public static MariadbTestConnection forTestDatabase(String databaseName) {
        return new MariadbTestConnection(JdbcConfiguration.copy(
                Configuration.fromSystemProperties(DATABASE_CONFIG_PREFIX).merge(Configuration.fromSystemProperties(DRIVER_CONFIG_PREFIX)))
                .withDatabase(databaseName)
                .with("characterEncoding", "utf8")
                .build());
    }

    /**
     * Obtain a connection instance to the named test database.
     * @param databaseName the name of the test database
     * @param urlProperties url properties
     * @return the MySQLConnection instance; never null
     */
    public static MariadbTestConnection forTestDatabase(String databaseName, Map<String, Object> urlProperties) {
        JdbcConfiguration.Builder builder = JdbcConfiguration.copy(
                Configuration.fromSystemProperties(DATABASE_CONFIG_PREFIX).merge(Configuration.fromSystemProperties(DRIVER_CONFIG_PREFIX)))
                .withDatabase(databaseName)
                .with("characterEncoding", "utf8");
        urlProperties.forEach(builder::with);
        return new MariadbTestConnection(builder.build());
    }

    /**
     * Obtain a connection instance to the named test database.
     *
     * @param databaseName the name of the test database
     * @param username the username
     * @param password the password
     * @return the MySQLConnection instance; never null
     */
    public static MariadbTestConnection forTestDatabase(String databaseName, String username, String password) {
        return new MariadbTestConnection(JdbcConfiguration.copy(
                Configuration.fromSystemProperties(DATABASE_CONFIG_PREFIX).merge(Configuration.fromSystemProperties(DRIVER_CONFIG_PREFIX)))
                .withDatabase(databaseName)
                .withUser(username)
                .withPassword(password)
                .build());
    }

    /**
     * Obtain whether the database source is MySQL 5.x or not.
     *
     * @return true if the database version is 5.x; otherwise false.
     */
    public static boolean isMySQL5() {
        return false;
    }

    /**
     * Obtain whether the database source is the Percona Server fork.
     *
     * @return true if the database is Percona Server; otherwise false.
     */
    public static boolean isPerconaServer() {
        String comment = forTestDatabase("mysql").getMySqlVersionComment();
        return comment.startsWith("Percona");
    }

    private static JdbcConfiguration addDefaultSettings(JdbcConfiguration configuration) {
        return JdbcConfiguration.adapt(configuration.edit()
                .withDefault(JdbcConfiguration.HOSTNAME, "localhost")
                .withDefault(JdbcConfiguration.PORT, 3306)
                .withDefault(JdbcConfiguration.USER, "mysqluser")
                .withDefault(JdbcConfiguration.PASSWORD, "mysqlpw")
                .build());

    }

    protected static ConnectionFactory FACTORY = JdbcConnection.patternBasedFactory("jdbc:mysql://${hostname}:${port}/${dbname}");

    /**
     * Create a new instance with the given configuration and connection factory.
     *
     * @param config the configuration; may not be null
     */
    public MariadbTestConnection(JdbcConfiguration config) {
        super(addDefaultSettings(config), FACTORY, "`", "`");
    }

    public MariaDbVersion getMariaDbVersion() {
        if (mariaDbVersion == null) {
            final String versionString = getMariaDbVersionString();
            if (versionString.startsWith("10.3")) {
                mariaDbVersion = MariaDbVersion.MARIADB_10_3;
            }
            else {
                throw new IllegalStateException("Couldn't resolve MySQL Server version");
            }
        }

        return mariaDbVersion;
    }

    public String getMariaDbVersionString() {
        String versionString;
        try {
            versionString = connect().queryAndMap("SHOW GLOBAL VARIABLES LIKE 'version'", rs -> {
                rs.next();
                return rs.getString(2);
            });
        }
        catch (SQLException e) {
            throw new IllegalStateException("Couldn't obtain MariaDb Server version", e);
        }
        return versionString;
    }

    public String getMySqlVersionComment() {
        String versionString;
        try {
            versionString = connect().queryAndMap("SHOW GLOBAL VARIABLES LIKE 'version_comment'", rs -> {
                rs.next();
                return rs.getString(2);
            });
        }
        catch (SQLException e) {
            throw new IllegalStateException("Couldn't obtain MySQL Server version comment", e);
        }
        return versionString;
    }

    public boolean isTableIdCaseSensitive() {
        String caseString;
        try {
            caseString = connect().queryAndMap("SHOW GLOBAL VARIABLES LIKE '" + MySqlSystemVariables.LOWER_CASE_TABLE_NAMES + "'", rs -> {
                rs.next();
                return rs.getString(2);
            });
        }
        catch (SQLException e) {
            throw new IllegalStateException("Couldn't obtain MySQL Server version comment", e);
        }
        return !"0".equals(caseString);
    }

    public DatabaseDifferences databaseAsserts() {
        if (databaseAsserts == null) {
            if (getMariaDbVersion() == MariaDbVersion.MARIADB_10_3) {
                databaseAsserts = new DatabaseDifferences() {
                    @Override
                    public boolean isCurrentDateTimeDefaultGenerated() {
                        return true;
                    }

                    @Override
                    public String currentDateTimeDefaultOptional(String isoString) {
                        return null;
                    }
                };
            }
            else {
                databaseAsserts = new DatabaseDifferences() {
                    @Override
                    public boolean isCurrentDateTimeDefaultGenerated() {
                        return false;
                    }

                    @Override
                    public String currentDateTimeDefaultOptional(String isoString) {
                        return isoString;
                    }

                };
            }
        }
        return databaseAsserts;
    }
}
