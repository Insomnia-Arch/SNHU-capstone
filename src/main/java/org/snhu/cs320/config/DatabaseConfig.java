package org.snhu.cs320.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;

public final class DatabaseConfig {

    private DatabaseConfig() {}

    public static DataSource createDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setURL(requireEnvironmentVariable("CS499_DB_URL"));
        dataSource.setUser(requireEnvironmentVariable("CS499_DB_USER"));
        dataSource.setPassword(requireEnvironmentVariable("CS499_DB_PASSWORD"));

        return dataSource;
    }

    private static String requireEnvironmentVariable(String name) {
        String value = System.getenv(name);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                "Missing required environment variable: " + name
            );
        }
        return value;
    }
}
