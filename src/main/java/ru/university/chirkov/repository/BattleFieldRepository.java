package ru.university.chirkov.repository;

import ru.university.chirkov.model.BattleField;

public interface BattleFieldRepository {
    void save(BattleField battleField);
    BattleField get();
    boolean update(BattleField battleField);
    boolean delete();
}