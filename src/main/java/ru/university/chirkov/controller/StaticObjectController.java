
package ru.university.chirkov.controller;

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

    private StaticObjectService service;
    private BattleFieldService battleFieldService;
    private CombatUnitService combatUnitService;
    private Scanner scanner;

    public StaticObjectController(
            StaticObjectService service,
            BattleFieldService battleFieldService,
            CombatUnitService combatUnitService,
            Scanner scanner) {

        this.service = service;
        this.battleFieldService = battleFieldService;
        this.combatUnitService = combatUnitService;
        this.scanner = scanner;
    }

    public void add() {
        BattleField field = battleFieldService.get();

        if (field == null) {
            System.out.println("Сначала создайте поле");
            return;
        }

        System.out.print("Введите название объекта: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым");
            return;
        }

        System.out.print("Введите координату X: ");
        int x = scanner.nextInt();

        System.out.print("Введите координату Y: ");
        int y = scanner.nextInt();

        if (x < 1 || x > field.getWidth()
                || y < 1 || y > field.getHeight()) {
            System.out.println(
                    "Координаты находятся за пределами поля");
            return;
        }

        for (StaticObject object : service.findAll()) {
            for (Cell cell : object.getCells()) {
                if (cell.getX() == x && cell.getY() == y) {
                    System.out.println(
                            "Клетка уже занята статическим объектом");
                    return;
                }
            }
        }

        for (CombatUnit unit : combatUnitService.findAll()) {
            for (Cell cell : unit.getCells()) {
                if (cell.getX() == x && cell.getY() == y) {
                    System.out.println(
                            "Клетка занята боевой единицей");
                    return;
                }
            }
        }

        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(x, y));

        service.add(new StaticObject(name, cells));

        System.out.println("Объект добавлен");
    }

    public void showAll() {
        List<StaticObject> objects = service.findAll();

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

        StaticObject object = service.findById(id);

        if (object == null) {
            System.out.println("Объект не найден");
            return;
        }

        System.out.print("Введите новое название: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Название не может быть пустым");
            return;
        }

        object.setName(name);
        service.update(object);

        System.out.println("Объект обновлён");
    }

    public void remove() {
        System.out.print("Введите ID объекта: ");
        int id = scanner.nextInt();

        if (service.remove(id)) {
            System.out.println("Объект удалён");
        } else {
            System.out.println("Объект не найден");
        }
    }
}