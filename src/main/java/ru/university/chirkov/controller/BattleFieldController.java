package ru.university.chirkov.controller;

import ru.university.chirkov.exception.BusinessException;
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
        if (battleFieldService.get() != null) {
            throw new BusinessException(
                    "Поле уже существует. Измените его или удалите перед созданием нового."
            );
        }

        System.out.print("Введите ширину поля: ");
        int width = scanner.nextInt();

        System.out.print("Введите высоту поля: ");
        int height = scanner.nextInt();

        if (width <= 0 || height <= 0) {
            throw new BusinessException(
                    "Ширина и высота должны быть больше нуля"
            );
        }

        battleFieldService.save(
                new BattleField(width, height, new ArrayList<>())
        );

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
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            throw new BusinessException("Сначала создайте поле");
        }

        System.out.print("Введите новую ширину поля: ");
        int width = scanner.nextInt();

        System.out.print("Введите новую высоту поля: ");
        int height = scanner.nextInt();

        if (width <= 0 || height <= 0) {
            throw new BusinessException(
                    "Размеры должны быть больше нуля"
            );
        }

        for (var unit : combatUnitService.findAll()) {
            for (var cell : unit.getCells()) {
                if (cell.getX() > width || cell.getY() > height) {
                    throw new BusinessException(
                            "Нельзя уменьшить поле: за его пределами находится юнит"
                    );
                }
            }
        }

        for (var object : staticObjectService.findAll()) {
            for (var cell : object.getCells()) {
                if (cell.getX() > width || cell.getY() > height) {
                    throw new BusinessException(
                            "Нельзя уменьшить поле: за его пределами находится объект"
                    );
                }
            }
        }

        battleFieldService.update(
                new BattleField(width, height, new ArrayList<>())
        );

        System.out.println("Поле обновлено");
    }

    public void delete() {
        if (battleFieldService.get() == null) {
            throw new BusinessException("Поле ещё не создано");
        }

        combatUnitService.deleteAll();
        staticObjectService.deleteAll();
        battleFieldService.delete();

        System.out.println("Поле удалено");
    }
}