package ru.university.chirkov.repository;

import ru.university.chirkov.model.Cell;
import ru.university.chirkov.model.CombatUnit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CombatUnitRepositoryDatabaseImpl implements CombatUnitRepository {

    private final DataSource dataSource;

    public CombatUnitRepositoryDatabaseImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(CombatUnit unit) {
        String sql = "INSERT INTO combatunits (speed, health, attack_range, damage) "
                + "VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, unit.getSpeed());
                statement.setInt(2, unit.getHealth());
                statement.setInt(3, unit.getAttackRange());
                statement.setInt(4, unit.getDamage());

                int id;
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new SQLException("Не удалось получить id боевой единицы");
                    }
                    id = resultSet.getInt(1);
                }

                saveCells(connection, id, unit.getCells());
                connection.commit();
                unit.setId(id);
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось добавить боевую единицу", e);
        }
    }

    @Override
    public List<CombatUnit> findAll() {
        String sql = "SELECT id, speed, health, attack_range, damage "
                + "FROM combatunits ORDER BY id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<CombatUnit> units = new ArrayList<>();

            while (resultSet.next()) {
                List<Cell> cells = getCells(connection, resultSet.getInt("id"));
                units.add(toUnit(resultSet, cells));
            }

            return units;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить боевые единицы", e);
        }
    }

    @Override
    public CombatUnit findById(int id) {
        String sql = "SELECT id, speed, health, attack_range, damage "
                + "FROM combatunits WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return toUnit(resultSet, getCells(connection, id));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти боевую единицу", e);
        }
    }

    @Override
    public boolean update(CombatUnit unit) {
        String sql = "UPDATE combatunits "
                + "SET speed = ?, health = ?, attack_range = ?, damage = ? "
                + "WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, unit.getSpeed());
                statement.setInt(2, unit.getHealth());
                statement.setInt(3, unit.getAttackRange());
                statement.setInt(4, unit.getDamage());
                statement.setInt(5, unit.getId());

                if (statement.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }

                deleteCells(connection, unit.getId());
                saveCells(connection, unit.getId(), unit.getCells());
                connection.commit();
                return true;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить боевую единицу", e);
        }
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM combatunits WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить боевую единицу", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM combatunits";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить боевые единицы", e);
        }
    }

    private void saveCells(Connection connection, int unitId, List<Cell> cells)
            throws SQLException {
        String sql = "INSERT INTO combatunits_cells (combatunits_id, x, y) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Cell cell : cells) {
                statement.setInt(1, unitId);
                statement.setInt(2, cell.getX());
                statement.setInt(3, cell.getY());
                statement.executeUpdate();
            }
        }
    }

    private void deleteCells(Connection connection, int unitId) throws SQLException {
        String sql = "DELETE FROM combatunits_cells WHERE combatunits_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, unitId);
            statement.executeUpdate();
        }
    }

    private List<Cell> getCells(Connection connection, int unitId) throws SQLException {
        String sql = "SELECT x, y FROM combatunits_cells "
                + "WHERE combatunits_id = ? ORDER BY id";

        List<Cell> cells = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, unitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cells.add(new Cell(resultSet.getInt("x"), resultSet.getInt("y")));
                }
            }
        }

        return cells;
    }

    private CombatUnit toUnit(ResultSet resultSet, List<Cell> cells) throws SQLException {
        CombatUnit unit = new CombatUnit(cells, resultSet.getInt("speed"));
        unit.setId(resultSet.getInt("id"));
        unit.setHealth(resultSet.getInt("health"));
        unit.setAttackRange(resultSet.getInt("attack_range"));
        unit.setDamage(resultSet.getInt("damage"));
        return unit;
    }
}