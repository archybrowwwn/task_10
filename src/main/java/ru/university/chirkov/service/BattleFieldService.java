package ru.university.chirkov.service;

import ru.university.chirkov.model.BattleField;
import ru.university.chirkov.repository.BattleFieldRepositoryInMemoryImpl;

public class BattleFieldService {

    private BattleFieldRepositoryInMemoryImpl repository;

    public BattleFieldService(BattleFieldRepositoryInMemoryImpl repository) {
        this.repository = repository;
    }

    public void save(BattleField battleField) {
        repository.save(battleField);
    }

    public BattleField get() {
        return repository.get();
    }

    public void update(BattleField battleField) {
        repository.update(battleField);
    }

    public void delete() {
        repository.delete();
    }
}
