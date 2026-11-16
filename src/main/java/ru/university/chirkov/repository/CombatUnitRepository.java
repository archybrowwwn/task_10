package ru.university.chirkov.repository;

import ru.university.chirkov.model.CombatUnit;
import java.util.List;

public interface CombatUnitRepository {
    void add(CombatUnit unit);
    List<CombatUnit> findAll();
    CombatUnit findById(int id);
    boolean update(CombatUnit unit);
    boolean remove(int id);
    void deleteAll();
}