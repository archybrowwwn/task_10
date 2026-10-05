package ru.university.chirkov.service;

import ru.university.chirkov.model.BattleField;
import ru.university.chirkov.repository.BattleFieldRepository;

public class BattlefieldService {

    private BattleFieldRepository repository;

    public BattlefieldService(BattleFieldRepository repository) {
        this.repository = repository;
    }

    public void save(BattleField battleField) {
        repository.save(battleField);
    }

    public BattleField get() {
        return repository.get();
    }
}
