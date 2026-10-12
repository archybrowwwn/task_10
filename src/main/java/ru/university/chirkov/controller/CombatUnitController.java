
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

public class CombatUnitController {

    private CombatUnitService service;
    private BattleFieldService battleFieldService;
    private StaticObjectService staticObjectService;
    private Scanner scanner;

    public CombatUnitController(
            CombatUnitService service,
            BattleFieldService battleFieldService,
            StaticObjectService staticObjectService,
            Scanner scanner) {

        this.service = service;
        this.battleFieldService = battleFieldService;
        this.staticObjectService = staticObjectService;
        this.scanner = scanner;
    }

    public void add() {
        BattleField field = battleFieldService.get();

        if (field == null) {
            System.out.println("Сначала создайте поле");
            return;
        }

        System.out.print("Введите скорость боевой единицы: ");
        int speed = scanner.nextInt();

        System.out.print("Введите координату X: ");
        int x = scanner.nextInt();

        System.out.print("Введите координату Y: ");
        int y = scanner.nextInt();

        if (speed <= 0) {
            System.out.println("Скорость должна быть больше нуля");
            return;
        }

        if (x < 1 || x > field.getWidth()
                || y < 1 || y > field.getHeight()) {
            System.out.println("Координаты находятся за пределами поля");
            return;
        }

        for (CombatUnit unit : service.findAll()) {
            for (Cell cell : unit.getCells()) {
                if (cell.getX() == x && cell.getY() == y) {
                    System.out.println(
                            "Клетка уже занята боевой единицей");
                    return;
                }
            }
        }

        for (StaticObject object : staticObjectService.findAll()) {
            for (Cell cell : object.getCells()) {
                if (cell.getX() == x && cell.getY() == y) {
                    System.out.println(
                            "Клетка занята статическим объектом");
                    return;
                }
            }
        }

        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(x, y));

        service.add(new CombatUnit(cells, speed));

        System.out.println("Боевая единица добавлена");
    }

    public void showAll() {
        List<CombatUnit> units = service.findAll();

        if (units.isEmpty()) {
            System.out.println("Боевых единиц пока нет");
            return;
        }

        for (CombatUnit unit : units) {
            System.out.println(unit);
        }
    }

    public void update() {
        System.out.print("Введите ID боевой единицы: ");
        int id = scanner.nextInt();

        CombatUnit unit = service.findById(id);

        if (unit == null) {
            System.out.println("Боевая единица не найдена");
            return;
        }

        System.out.print("Введите новую скорость: ");
        int speed = scanner.nextInt();

        if (speed <= 0) {
            System.out.println("Скорость должна быть больше нуля");
            return;
        }

        unit.setSpeed(speed);
        service.update(unit);

        System.out.println("Боевая единица обновлена");
    }

    public void remove() {
        System.out.print("Введите ID боевой единицы: ");
        int id = scanner.nextInt();

        if (service.remove(id)) {
            System.out.println("Боевая единица удалена");
        } else {
            System.out.println("Боевая единица не найдена");
        }
    }
}