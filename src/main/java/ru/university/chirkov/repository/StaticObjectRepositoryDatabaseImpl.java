package ru.university.chirkov.repository;

import ru.university.chirkov.model.Cell;
import ru.university.chirkov.model.StaticObject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaticObjectRepositoryDatabaseImpl implements StaticObjectRepository {

    private final DataSource dataSource;

    public StaticObjectRepositoryDatabaseImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(StaticObject object) {
        String sql = "INSERT INTO staticobjects (name) VALUES (?) RETURNING id";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, object.getName());

                int id;
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new SQLException("Не удалось получить id статического объекта");
                    }
                    id = resultSet.getInt(1);
                }

                saveCells(connection, id, object.getCells());
                connection.commit();
                object.setId(id);
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось добавить статический объект", e);
        }
    }

    @Override
    public List<StaticObject> findAll() {
        String sql = "SELECT id, name FROM staticobjects ORDER BY id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<StaticObject> objects = new ArrayList<>();

            while (resultSet.next()) {
                List<Cell> cells = getCells(connection, resultSet.getInt("id"));
                objects.add(toObject(resultSet, cells));
            }

            return objects;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить статические объекты", e);
        }
    }

    @Override
    public StaticObject findById(int id) {
        String sql = "SELECT id, name FROM staticobjects WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return toObject(resultSet, getCells(connection, id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти статический объект", e);
        }
    }

    @Override
    public boolean update(StaticObject object) {
        String sql = "UPDATE staticobjects SET name = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, object.getName());
                statement.setInt(2, object.getId());

                if (statement.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }

                deleteCells(connection, object.getId());
                saveCells(connection, object.getId(), object.getCells());
                connection.commit();
                return true;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить статический объект", e);
        }
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM staticobjects WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить статический объект", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM staticobjects";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить статические объекты", e);
        }
    }

    private void saveCells(Connection connection, int objectId, List<Cell> cells)
            throws SQLException {
        String sql = "INSERT INTO staticobjects_cells (staticobjects_id, x, y) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Cell cell : cells) {
                statement.setInt(1, objectId);
                statement.setInt(2, cell.getX());
                statement.setInt(3, cell.getY());
                statement.executeUpdate();
            }
        }
    }

    private void deleteCells(Connection connection, int objectId) throws SQLException {
        String sql = "DELETE FROM staticobjects_cells WHERE staticobjects_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, objectId);
            statement.executeUpdate();
        }
    }

    private List<Cell> getCells(Connection connection, int objectId) throws SQLException {
        String sql = "SELECT x, y FROM staticobjects_cells "
                + "WHERE staticobjects_id = ? ORDER BY id";

        List<Cell> cells = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, objectId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cells.add(new Cell(resultSet.getInt("x"), resultSet.getInt("y")));
                }
            }
        }

        return cells;
    }

    private StaticObject toObject(ResultSet resultSet, List<Cell> cells) throws SQLException {
        StaticObject object = new StaticObject(resultSet.getString("name"), cells);
        object.setId(resultSet.getInt("id"));
        return object;
    }
}