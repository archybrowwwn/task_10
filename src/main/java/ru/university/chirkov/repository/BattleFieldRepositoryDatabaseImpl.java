package ru.university.chirkov.repository;

import ru.university.chirkov.model.BattleField;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BattleFieldRepositoryDatabaseImpl implements BattleFieldRepository {

    private static final int FIELD_ID = 1;

    private final DataSource dataSource;

    public BattleFieldRepositoryDatabaseImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(BattleField battleField) {
        String sql = "INSERT INTO battlefields (id, width, height) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, FIELD_ID);
            statement.setInt(2, battleField.getWidth());
            statement.setInt(3, battleField.getHeight());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить поле боя", e);
        }
    }

    @Override
    public BattleField get() {
        String sql = "SELECT width, height FROM battlefields WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, FIELD_ID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new BattleField(
                        resultSet.getInt("width"),
                        resultSet.getInt("height"),
                        new ArrayList<>());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить поле боя", e);
        }
    }

    @Override
    public boolean update(BattleField battleField) {
        String sql = "UPDATE battlefields SET width = ?, height = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, battleField.getWidth());
            statement.setInt(2, battleField.getHeight());
            statement.setInt(3, FIELD_ID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить поле боя", e);
        }
    }

    @Override
    public boolean delete() {
        String sql = "DELETE FROM battlefields WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, FIELD_ID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить поле боя", e);
        }
    }
}