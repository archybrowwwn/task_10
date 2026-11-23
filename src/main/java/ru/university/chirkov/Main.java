package ru.university.chirkov;

import ru.university.chirkov.config.DatabaseConfig;
import ru.university.chirkov.controller.BattleFieldController;
import ru.university.chirkov.controller.CmdController;
import ru.university.chirkov.controller.CombatUnitController;
import ru.university.chirkov.controller.StaticObjectController;
import ru.university.chirkov.repository.BattleFieldRepositoryDatabaseImpl;
import ru.university.chirkov.repository.CombatUnitRepositoryDatabaseImpl;
import ru.university.chirkov.repository.StaticObjectRepositoryDatabaseImpl;
import ru.university.chirkov.service.BattleFieldService;
import ru.university.chirkov.service.CombatUnitService;
import ru.university.chirkov.service.StaticObjectService;

import javax.sql.DataSource;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = DatabaseConfig.createDataSource();
        DatabaseConfig.testConnection(dataSource);
        DatabaseConfig.initializeDatabase(dataSource);

        Scanner scanner = new Scanner(System.in);

        BattleFieldService battleFieldService =
                new BattleFieldService(new BattleFieldRepositoryDatabaseImpl(dataSource));

        CombatUnitService combatUnitService =
                new CombatUnitService(new CombatUnitRepositoryDatabaseImpl(dataSource));

        StaticObjectService staticObjectService =
                new StaticObjectService(new StaticObjectRepositoryDatabaseImpl(dataSource));

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