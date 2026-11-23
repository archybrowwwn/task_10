package ru.university.chirkov.config;

import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void initializeDatabase(DataSource dataSource) {
        String sql = """
                CREATE TABLE IF NOT EXISTS battlefields (
                    id INT PRIMARY KEY,
                    width INT NOT NULL,
                    height INT NOT NULL
                );
                
                CREATE TABLE IF NOT EXISTS battlefield_cells (
                    id SERIAL PRIMARY KEY,
                    battlefield_id INT NOT NULL REFERENCES battlefields (id) ON DELETE CASCADE,
                    x INT NOT NULL,
                    y INT NOT NULL
                );
                
                CREATE TABLE IF NOT EXISTS combatunits (
                    id SERIAL PRIMARY KEY,
                    speed INT NOT NULL,
                    health INT NOT NULL,
                    attack_range INT NOT NULL,
                    damage INT NOT NULL
                );
                
                CREATE TABLE IF NOT EXISTS combatunits_cells (
                    id SERIAL PRIMARY KEY,
                    combatunits_id INT NOT NULL REFERENCES combatunits (id) ON DELETE CASCADE,
                    x INT NOT NULL,
                    y INT NOT NULL
                );
                
                CREATE TABLE IF NOT EXISTS staticobjects (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL
                );
                
                CREATE TABLE IF NOT EXISTS staticobjects_cells (
                    id SERIAL PRIMARY KEY,
                    staticobjects_id INT NOT NULL REFERENCES staticobjects (id) ON DELETE CASCADE,
                    x INT NOT NULL,
                    y INT NOT NULL
                );
                """;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблицы базы данных успешно инициализированы!");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации таблиц БД: " + e.getMessage(), e);
        }
    }
}