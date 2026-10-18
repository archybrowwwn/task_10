
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
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            System.out.println("Сначала создайте поле");
            return;
        }

        System.out.print("Введите скорость боевой единицы: ");
        int speed = scanner.nextInt();

        System.out.print("Введите дальность стрельбы: ");
        int attackRange = scanner.nextInt();

        System.out.print("Введите координату X: ");
        int x = scanner.nextInt();

        System.out.print("Введите координату Y: ");
        int y = scanner.nextInt();

        System. out.println("Введите ширину юнита");
        int width = scanner.nextInt();

        System.out.println("Введите высоту юнита");
        int height = scanner.nextInt();

        if (speed < 0) {
            System.out.println("Скорость не может быть отрицательной");
            return;
        }

        if (attackRange <= 0) {
            System.out.println("Дальность стрельбы должна быть больше нуля");
            return;
        }

        if (width <= 0 || height <= 0) {
            System.out.println("Размеры должны быть больше нуля");
            return;
        }

        if (x < 1 || y < 1
                || x + width - 1 > battleField.getWidth()
                || y + height - 1 > battleField.getHeight()) {
            System.out.println("Юнит выходит за пределы поля");
            return;
        }

        List<Cell> cells = new ArrayList<>();

        for (int row = y; row < y + height; row++) {
            for (int col = x; col < x + width; col++) {
                cells.add(new Cell(col, row));
            }
        }

        for (CombatUnit unit : service.findAll()) {
            for (Cell occupied : unit.getCells()) {
                for (Cell cell : cells) {
                    if (occupied.getX() == cell.getX() && occupied.getY() == cell.getY()) {
                        System.out.println("Клетка занята другой боевой единицей");
                        return;
                    }
                }
            }
        }

        for (StaticObject object : staticObjectService.findAll()) {
            for (Cell occupied : object.getCells()) {
                for (Cell cell : cells) {
                    if (occupied.getX() == cell.getX() && occupied.getY() == cell.getY()) {
                        System.out.println("Клетка занята статическим объектом");
                        return;
                    }
                }
            }
        }

        CombatUnit unit = new CombatUnit(cells, speed);
        unit.setAttackRange(attackRange);
        service.add(unit);

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

        if (speed < 0) {
            System.out.println("Скорость не может быть отрицательной");
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

    public void move() {
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            System.out.println("Сначала создайте поле");
            return;
        }

        System.out.print("Введите ID юнита: ");
        int id = scanner.nextInt();

        CombatUnit unit = service.findById(id);

        if (unit == null) {
            System.out.println("Юнит не найден");
            return;
        }

        if (unit.isDestroyed()) {
            System.out.println("Уничтоженный юнит не может двигаться");
            return;
        }

        if (unit.getSpeed() == 0) {
            System.out.println("Этот юнит неподвижен");
            return;
        }

        int currentX = unit.getCells().get(0).getX();
        int currentY = unit.getCells().get(0).getY();

        for (Cell cell : unit.getCells()) {
            currentX = Math.min(currentX, cell.getX());
            currentY = Math.min(currentY, cell.getY());
        }

        int width = 0;
        int height = 0;

        for (Cell cell : unit.getCells()) {
            width = Math.max(width, cell.getX() - currentX + 1);
            height = Math.max(height, cell.getY() - currentY + 1);
        }

        System.out.print("Введите новую X: ");
        int x = scanner.nextInt();

        System.out.print("Введите новую Y: ");
        int y = scanner.nextInt();

        if (x < 1 || y < 1
                || x + width - 1 > battleField.getWidth()
                || y + height - 1 > battleField.getHeight()) {
            System.out.println("Координаты за пределами поля");
            return;
        }

        if (x != currentX && y != currentY) {
            System.out.println("Двигаться можно только по горизонтали или вертикали");
            return;
        }

        int dx = x - currentX;
        int dy = y - currentY;

        int distance = Math.abs(dx) + Math.abs(dy);

        if (distance == 0 || distance > unit.getSpeed()) {
            System.out.println("Недопустимое расстояние");
            return;
        }

        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        int stepX = Integer.compare(dx, 0);
        int stepY = Integer.compare(dy, 0);

        for (int step = 1; step <= steps; step++) {
            int offsetX = stepX * step;
            int offsetY = stepY * step;

            for (Cell oldCell : unit.getCells()) {
                int nextX = oldCell.getX() + offsetX;
                int nextY = oldCell.getY() + offsetY;

                for (CombatUnit other : service.findAll()) {
                    if (other.getId() == id) {
                        continue;
                    }

                    for (Cell cell : other.getCells()) {
                        if (cell.getX() == nextX && cell.getY() == nextY) {
                            System.out.println("Путь занят другим юнитом");
                            return;
                        }
                    }
                }

                for (StaticObject object : staticObjectService.findAll()) {
                    for (Cell cell : object.getCells()) {
                        if (cell.getX() == nextX && cell.getY() == nextY) {
                            System.out.println("Путь заблокирован объектом");
                            return;
                        }
                    }
                }
            }
        }

        List<Cell> newCells = new ArrayList<>();

        for (Cell cell : unit.getCells()) {
            newCells.add(new Cell(
                    cell.getX() + dx,
                    cell.getY() + dy
            ));
        }

        unit.setCells(newCells);
        service.update(unit);

        System.out.println("Юнит перемещён");
    }

    public void fire() {
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            System.out.println("Сначала создайте поле");
            return;
        }

        System.out.print("ID стреляющего юнита: ");
        int shooterId = scanner.nextInt();

        CombatUnit shooter = service.findById(shooterId);

        if (shooter == null) {
            System.out.println("Стреляющий юнит не найден");
            return;
        }

        if (shooter.isDestroyed()) {
            System.out.println("Уничтоженный юнит не может стрелять");
            return;
        }

        System.out.println("Направление выстрела:");
        System.out.println("1 - вверх");
        System.out.println("2 - вниз");
        System.out.println("3 - влево");
        System.out.println("4 - вправо");
        System.out.println("Ваш выбор: ");

        int direction = scanner.nextInt();

        int dx;
        int dy;

        switch (direction) {
            case 1:
                dx = 0;
                dy = -1;
                break;
            case 2:
                dx = 0;
                dy = 1;
                break;
            case 3:
                dx = -1;
                dy = 0;
                break;
            case 4:
                dx = 1;
                dy = 0;
                break;
            default:
                System.out.println("Неизвестное направление");
                return;
        }

        int startX = shooter.getCells().get(0).getX();
        int startY = shooter.getCells().get(0).getY();

        for (Cell cell : shooter.getCells()) {
            if (direction == 1) {
                startY = Math.min(startY, cell.getY());
            } else if (direction == 2) {
                startY = Math.max(startY, cell.getY());
            } else if (direction == 3) {
                startX = Math.min(startX, cell.getX());
            } else if (direction == 4) {
                startX = Math.max(startX, cell.getX());
            }
        }

        for (int distance = 1;
             distance <= shooter.getAttackRange();
             distance++) {

            int x = startX + dx * distance;
            int y = startY + dy * distance;

            if (x < 1 || x > battleField.getWidth()
                    || y < 1 || y > battleField.getHeight()) {
                System.out.println("Снаряд покинул пределы поля");
                return;
            }

            for (StaticObject object : staticObjectService.findAll()) {
                for (Cell cell : object.getCells()) {
                    if (cell.getX() == x && cell.getY() == y) {
                        System.out.println(
                                "Снаряд попал в препятствие: " + cell.getX()
                        );
                        return;
                    }
                }
            }

            for (CombatUnit target : service.findAll()) {
                if (target.getId() == shooterId || target.isDestroyed()) {
                    continue;
                }

                boolean hit = false;

                for (Cell cell : target.getCells()) {
                    if (cell.getX() == x && cell.getY() == y) {
                        hit = true;
                        break;
                    }
                }

                if (hit) {
                    int oldHealth = target.getHealth();

                    target.setHealth(
                            target.getHealth() - shooter.getDamage()
                    );

                    System.out.println(
                            "Попадание в юнита #" + target.getId()
                    );
                    System.out.println(
                            "Здоровье: " + oldHealth + " -> " + target.getHealth()
                    );

                    if (target.isDestroyed()) {
                        service.remove(target.getId());
                        System.out.println(
                                "Юнит #" + target.getId() + " уничтожен"
                        );
                    }

                    return;
                }
            }
        }

        System.out.println("Выстрел не достиг цели");
    }
}