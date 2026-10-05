package ru.university.chirkov.repository;

import ru.university.chirkov.model.CombatUnit;

import java.util.ArrayList;
import java.util.List;

public class CombatUnitRepository {

    private List<CombatUnit> units = new ArrayList<>();

    public void add(CombatUnit unit) {
        units.add(unit);
    }

    public List<CombatUnit> findAll() {
        return units;
    }

    public void remove(CombatUnit unit) {
        units.remove(unit);
    }
}
