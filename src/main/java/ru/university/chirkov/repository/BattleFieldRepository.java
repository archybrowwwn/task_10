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

    public boolean update(BattleField battleField) {
        if (this.battleField == null) {
            return false;
        }

        this.battleField = battleField;
        return true;
    }

    public boolean delete() {
        if (battleField == null) {
            return false;
        }

        battleField = null;
        return true;
    }
}
