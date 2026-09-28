package ru.university.chirkov.repository;

import ru.university.chirkov.model.BattleField;

public class BattleFieldRepository {

    private BattleField battleField;

    public void save(BattleField battleField) {
        this.battleField = battleField;
    }

    public BattleField get() {
        return battleField;
    }
}
