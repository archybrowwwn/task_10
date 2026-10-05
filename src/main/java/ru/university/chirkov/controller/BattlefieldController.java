package ru.university.chirkov.controller;

import ru.university.chirkov.service.BattlefieldService;
import ru.university.chirkov.service.CombatUnitService;
import ru.university.chirkov.service.StaticObjectService;

public class BattlefieldController {

    private BattlefieldService battlefieldService;
    private CombatUnitService combatUnitService;
    private StaticObjectService staticObjectService;

    public BattlefieldController(
        BattlefieldService battlefieldService,
        CombatUnitService combatUnitService,
        StaticObjectService staticObjectService
    ) {
        this.battlefieldService = battlefieldService;
        this.combatUnitService = combatUnitService;
        this.staticObjectService = staticObjectService;
    }

    public void start() {
        System.out.println("Battlefield start");
    }
}
