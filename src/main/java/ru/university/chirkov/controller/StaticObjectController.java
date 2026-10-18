
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
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
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

        System.out.println("Введите ширину объекта: ");
        int width = scanner.nextInt();

        System.out.println("Введите высоту объекта: ");
        int height = scanner.nextInt();

        if (width <= 0 || height <= 0) {
            System.out.println("Размеры должны быть больше нуля");
            return;
        }

        if (x < 1 || y < 1
                || x + width - 1 > battleField.getWidth()
                || y + height - 1 > battleField.getHeight()) {
            System.out.println("Объект выходит за границы поля");
            return;
        }

        List<Cell> cells = new ArrayList<>();

        for (int row = y; row < y + height; row++) {
            for (int col = x; col < x + width; col++) {
                cells.add(new Cell(col, row));
            }
        }

        for (StaticObject object : service.findAll()) {
            for (Cell occupied : object.getCells()) {
                for (Cell cell : cells) {
                    if (occupied.getX() == cell.getX()
                            && occupied.getY() == cell.getY()) {
                        System.out.println("Клетка занята статическим объектом");
                        return;
                    }
                }
            }
        }

        for (CombatUnit unit : combatUnitService.findAll()) {
            for (Cell occupied : unit.getCells()) {
                for (Cell cell : cells) {
                    if (occupied.getX() == cell.getX()
                            && occupied.getY() == cell.getY()) {
                        System.out.println("Клетка занята боевой единицей");
                        return;
                    }
                }
            }
        }

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