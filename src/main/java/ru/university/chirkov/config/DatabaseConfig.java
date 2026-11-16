package ru.university.chirkov.config;

import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {

    public static DataSource createDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"localhost"});
        dataSource.setPortNumbers(new int[]{5433});
        dataSource.setDatabaseName("battlefield");
        dataSource.setUser("battlefield");
        dataSource.setPassword("battlefield");
        return dataSource;
    }

    public static void testConnection(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                System.out.println("Успешное подключение к PostgreSQL!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к PostgreSQL: " + e.getMessage(), e);
        }
    }
}