package ru.university.chirkov.repository;

import ru.university.chirkov.model.CombatUnit;

import java.util.ArrayList;
import java.util.List;

public class CombatUnitRepository {

    private List<CombatUnit> units = new ArrayList<>();
    private int nextId = 1;

    public void add(CombatUnit unit) {
        unit.setId(nextId++);
        units.add(unit);
    }

    public List<CombatUnit> findAll() {
        return units;
    }

    public CombatUnit findById(int id) {
        for (CombatUnit unit : units) {
            if (unit.getId() == id) {
                return unit;
            }
        }
        return null;
    }

    public boolean update(CombatUnit unit) {
        int index = units.indexOf(findById(unit.getId()));

        if (index == -1) {
            return false;
        }

        units.set(index, unit);
        return true;
    }

    public boolean remove(int id) {
        return units.remove(findById(id));
    }
}
