package ru.university.chirkov;

import ru.university.chirkov.config.DatabaseConfig;
import ru.university.chirkov.controller.BattleFieldController;
import ru.university.chirkov.controller.CmdController;
import ru.university.chirkov.controller.CombatUnitController;
import ru.university.chirkov.controller.StaticObjectController;
import ru.university.chirkov.repository.BattleFieldRepositoryInMemoryImpl;
import ru.university.chirkov.repository.CombatUnitRepositoryInMemoryImpl;
import ru.university.chirkov.repository.StaticObjectRepositoryInMemoryImpl;
import ru.university.chirkov.service.BattleFieldService;
import ru.university.chirkov.service.CombatUnitService;
import ru.university.chirkov.service.StaticObjectService;

import javax.sql.DataSource;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = DatabaseConfig.createDataSource();
        DatabaseConfig.testConnection(dataSource);

        Scanner scanner = new Scanner(System.in);

        BattleFieldService battleFieldService =
                new BattleFieldService(new BattleFieldRepositoryInMemoryImpl());

        CombatUnitService combatUnitService =
                new CombatUnitService(new CombatUnitRepositoryInMemoryImpl());

        StaticObjectService staticObjectService =
                new StaticObjectService(new StaticObjectRepositoryInMemoryImpl());

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