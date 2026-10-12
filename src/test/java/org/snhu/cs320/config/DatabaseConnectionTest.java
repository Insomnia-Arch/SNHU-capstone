package org.snhu.cs320.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "CS499_DB_URL", matches = ".+")
public class DatabaseConnectionTest {

    @Test
    void connectsAndReadsContactsTable() throws Exception {
        try (
            Connection connection = DatabaseConfig
                .createDataSource()
                .getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM contacts"
            );
            ResultSet results = statement.executeQuery()
        ) {
            assertTrue(results.next());
            assertTrue(results.getLong(1) >= 0);
        }
    }
}
