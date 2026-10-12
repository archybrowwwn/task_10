package ru.university.chirkov;

import ru.university.chirkov.controller.BattleFieldController;
import ru.university.chirkov.controller.CmdController;
import ru.university.chirkov.controller.CombatUnitController;
import ru.university.chirkov.controller.StaticObjectController;
import ru.university.chirkov.repository.BattleFieldRepository;
import ru.university.chirkov.repository.CombatUnitRepository;
import ru.university.chirkov.repository.StaticObjectRepository;
import ru.university.chirkov.service.BattleFieldService;
import ru.university.chirkov.service.CombatUnitService;
import ru.university.chirkov.service.StaticObjectService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BattleFieldService battleFieldService =
                new BattleFieldService(new BattleFieldRepository());

        CombatUnitService combatUnitService =
                new CombatUnitService(new CombatUnitRepository());

        StaticObjectService staticObjectService =
                new StaticObjectService(new StaticObjectRepository());

        BattleFieldController battleFieldController =
                new BattleFieldController(
                        battleFieldService,
                        combatUnitService,
                        staticObjectService,
                        scanner);

        CombatUnitController combatUnitController =
                new CombatUnitController(
                        combatUnitService,
                        battleFieldService,
                        staticObjectService,
                        scanner);

        StaticObjectController staticObjectController =
                new StaticObjectController(
                        staticObjectService,
                        battleFieldService,
                        combatUnitService,
                        scanner);

        CmdController cmdController =
                new CmdController(
                        battleFieldController,
                        combatUnitController,
                        staticObjectController,
                        scanner);

        cmdController.run();
    }
}