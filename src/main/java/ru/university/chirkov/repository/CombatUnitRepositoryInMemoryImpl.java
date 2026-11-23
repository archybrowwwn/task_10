package ru.university.chirkov.repository;

import ru.university.chirkov.model.CombatUnit;

import java.util.ArrayList;
import java.util.List;

public class CombatUnitRepositoryInMemoryImpl implements CombatUnitRepository{

    private List<CombatUnit> units = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void add(CombatUnit unit) {
        unit.setId(nextId++);
        units.add(unit);
    }

    @Override
    public List<CombatUnit> findAll() {
        return units;
    }

    @Override
    public CombatUnit findById(int id) {
        for (CombatUnit unit : units) {
            if (unit.getId() == id) {
                return unit;
            }
        }
        return null;
    }

    @Override
    public boolean update(CombatUnit unit) {
        int index = units.indexOf(findById(unit.getId()));

        if (index == -1) {
            return false;
        }

        units.set(index, unit);
        return true;
    }

    @Override
    public boolean remove(int id) {
        return units.remove(findById(id));
    }

    @Override
    public void deleteAll() {
        units.clear();
    }
}
