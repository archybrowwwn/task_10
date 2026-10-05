package ru.university.chirkov.controller;

import ru.university.chirkov.model.CombatUnit;
import ru.university.chirkov.service.CombatUnitService;

import java.util.List;

public class CombatUnitController {

    private CombatUnitService service;

    public CombatUnitController(CombatUnitService service) {
        this.service = service;
    }

    public void add(CombatUnit unit) {
        service.add(unit);
    }

    public List<CombatUnit> findAll() {
        return service.findAll();
    }

    private void remove(CombatUnit unit) {
        service.remove(unit);
    }
}
