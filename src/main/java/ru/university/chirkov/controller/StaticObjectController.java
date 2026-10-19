package ru.university.chirkov.controller;

import ru.university.chirkov.exception.BusinessException;
import ru.university.chirkov.exception.StaticObjectNameException;
import ru.university.chirkov.exception.StaticObjectNotFoundException;
import ru.university.chirkov.model.BattleField;
import ru.university.chirkov.model.Cell;
import ru.university.chirkov.model.CombatUnit;
import ru.university.chirkov.model.StaticObject;
import ru.university.chirkov.service.BattleFieldService;
import ru.university.chirkov.service.CombatUnitService;
import ru.university.chirkov.service.StaticObjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaticObjectController {

    private StaticObjectService staticObjectService;
    private BattleFieldService battleFieldService;
    private CombatUnitService combatUnitService;
    private Scanner scanner;

    public StaticObjectController(
            StaticObjectService staticObjectService,
            BattleFieldService battleFieldService,
            CombatUnitService combatUnitService,
            Scanner scanner) {

        this.staticObjectService = staticObjectService;
        this.battleFieldService = battleFieldService;
        this.combatUnitService = combatUnitService;
        this.scanner = scanner;
    }

    public void add() {
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            throw new BusinessException("Сначала создайте поле");
        }

        System.out.print("Введите название объекта: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            throw new StaticObjectNameException();
        }

        System.out.print("Введите координату X: ");
        int x = scanner.nextInt();

        System.out.print("Введите координату Y: ");
        int y = scanner.nextInt();

        System.out.print("Введите ширину объекта: ");
        int width = scanner.nextInt();

        System.out.print("Введите высоту объекта: ");
        int height = scanner.nextInt();

        if (width <= 0 || height <= 0) {
            throw new BusinessException(
                    "Размеры должны быть больше нуля"
            );
        }

        if (x < 1 || y < 1
                || x + width - 1 > battleField.getWidth()
                || y + height - 1 > battleField.getHeight()) {
            throw new BusinessException(
                    "Объект выходит за границы поля"
            );
        }

        List<Cell> cells = new ArrayList<>();

        for (int row = y; row < y + height; row++) {
            for (int col = x; col < x + width; col++) {
                cells.add(new Cell(col, row));
            }
        }

        for (StaticObject object : staticObjectService.findAll()) {
            for (Cell occupied : object.getCells()) {
                for (Cell cell : cells) {
                    if (occupied.getX() == cell.getX()
                            && occupied.getY() == cell.getY()) {
                        throw new BusinessException(
                                "Клетка занята статическим объектом"
                        );
                    }
                }
            }
        }

        for (CombatUnit unit : combatUnitService.findAll()) {
            for (Cell occupied : unit.getCells()) {
                for (Cell cell : cells) {
                    if (occupied.getX() == cell.getX()
                            && occupied.getY() == cell.getY()) {
                        throw new BusinessException(
                                "Клетка занята боевой единицей"
                        );
                    }
                }
            }
        }

        staticObjectService.add(new StaticObject(name, cells));

        System.out.println("Объект добавлен");
    }

    public void showAll() {
        List<StaticObject> objects = staticObjectService.findAll();

        if (objects.isEmpty()) {
            System.out.println("Статических объектов пока нет");
            return;
        }

        for (StaticObject object : objects) {
            System.out.println(object);
        }
    }

    public void update() {
        System.out.print("Введите ID объекта: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        StaticObject object = staticObjectService.findById(id);

        if (object == null) {
            throw new StaticObjectNotFoundException(id);
        }

        System.out.print("Введите новое название: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            throw new StaticObjectNameException();
        }

        object.setName(name);
        staticObjectService.update(object);

        System.out.println("Объект обновлён");
    }

    public void remove() {
        System.out.print("Введите ID объекта: ");
        int id = scanner.nextInt();

        if (!staticObjectService.remove(id)) {
            throw new StaticObjectNotFoundException(id);
        }

        System.out.println("Объект удалён");
    }
}