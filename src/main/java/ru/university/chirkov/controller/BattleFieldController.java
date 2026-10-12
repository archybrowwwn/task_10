package ru.university.chirkov.controller;

import ru.university.chirkov.model.BattleField;
import ru.university.chirkov.service.BattleFieldService;
import ru.university.chirkov.service.CombatUnitService;
import ru.university.chirkov.service.StaticObjectService;
import ru.university.chirkov.ui.ConsoleBattleFieldRenderer;

import java.util.ArrayList;
import java.util.Scanner;

public class BattleFieldController {

    private BattleFieldService battleFieldService;
    private CombatUnitService combatUnitService;
    private StaticObjectService staticObjectService;
    private Scanner scanner;
    private ConsoleBattleFieldRenderer renderer =
            new ConsoleBattleFieldRenderer();

    public BattleFieldController(
            BattleFieldService service,
            CombatUnitService combatUnitService,
            StaticObjectService staticObjectService,
            Scanner scanner) {

        this.battleFieldService = service;
        this.combatUnitService = combatUnitService;
        this.staticObjectService = staticObjectService;
        this.scanner = scanner;
    }

    public void create() {
        System.out.println("Введите ширину поля: ");
        int width = scanner.nextInt();

        System.out.print("Введите высоту поля: ");
        int height = scanner.nextInt();

        battleFieldService.save(new BattleField(width, height, new ArrayList<>()));
        System.out.println("Поле создано");
    }

    public void show() {
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            System.out.println("Поле ещё не создано");
            return;
        }

        renderer.render(
                battleField,
                combatUnitService.findAll(),
                staticObjectService.findAll()
        );
    }

    public void update() {
        if (battleFieldService.get() == null) {
            System.out.println("Сначала создайте поле");
            return;
        }

        create();
    }

    public void delete() {
        battleFieldService.delete();
        System.out.println("Поле удалено");
    }
}
