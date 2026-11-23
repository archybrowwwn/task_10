package ru.university.chirkov.repository;

import ru.university.chirkov.model.BattleField;

public class BattleFieldRepositoryInMemoryImpl implements BattleFieldRepository{

    private BattleField battleField;

    @Override
    public void save(BattleField battleField) {
        this.battleField = battleField;
    }

    @Override
    public BattleField get() {
        return battleField;
    }

    @Override
    public boolean update(BattleField battleField) {
        if (this.battleField == null) {
            return false;
        }

        this.battleField = battleField;
        return true;
    }

    @Override
    public boolean delete() {
        if (battleField == null) {
            return false;
        }

        battleField = null;
        return true;
    }
}
