package ru.university.chirkov.service;

import ru.university.chirkov.model.CombatUnit;
import ru.university.chirkov.repository.CombatUnitRepositoryInMemoryImpl;

import java.util.List;

public class CombatUnitService {

    private CombatUnitRepositoryInMemoryImpl repository;

    public CombatUnitService(CombatUnitRepositoryInMemoryImpl repository) {
        this.repository = repository;
    }

    public void add(CombatUnit unit) {
        repository.add(unit);
    }

    public List<CombatUnit> findAll() {
        return repository.findAll();
    }

    public CombatUnit findById(int id) {
        return repository.findById(id);
    }

    public boolean update(CombatUnit unit) {
        return repository.update(unit);
    }

    public boolean remove(int id) {
        return repository.remove(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
