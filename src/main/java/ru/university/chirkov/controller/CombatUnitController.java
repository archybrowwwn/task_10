package ru.university.chirkov.controller;

import ru.university.chirkov.exception.BusinessException;
import ru.university.chirkov.exception.CombatUnitNotFoundException;
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

    private CombatUnitService combatUnitService;
    private BattleFieldService battleFieldService;
    private StaticObjectService staticObjectService;
    private Scanner scanner;

    public CombatUnitController(
            CombatUnitService combatUnitService,
            BattleFieldService battleFieldService,
            StaticObjectService staticObjectService,
            Scanner scanner) {

        this.combatUnitService = combatUnitService;
        this.battleFieldService = battleFieldService;
        this.staticObjectService = staticObjectService;
        this.scanner = scanner;
    }

    public void add() {
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            throw new BusinessException("Сначала создайте поле");
        }

        System.out.print("Введите скорость боевой единицы: ");
        int speed = scanner.nextInt();

        System.out.print("Введите дальность стрельбы: ");
        int attackRange = scanner.nextInt();

        System.out.print("Введите координату X: ");
        int x = scanner.nextInt();

        System.out.print("Введите координату Y: ");
        int y = scanner.nextInt();

        System.out.println("Введите ширину юнита");
        int width = scanner.nextInt();

        System.out.println("Введите высоту юнита");
        int height = scanner.nextInt();

        if (speed < 0) {
            throw new BusinessException(
                    "Скорость не может быть отрицательной"
            );
        }

        if (attackRange <= 0) {
            throw new BusinessException(
                    "Дальность стрельбы должна быть больше нуля"
            );
        }

        if (width <= 0 || height <= 0) {
            throw new BusinessException(
                    "Размеры должны быть больше нуля"
            );
        }

        if (x < 1 || y < 1
                || x + width - 1 > battleField.getWidth()
                || y + height - 1 > battleField.getHeight()) {
            throw new BusinessException(
                    "Юнит выходит за пределы поля"
            );
        }

        List<Cell> cells = new ArrayList<>();

        for (int row = y; row < y + height; row++) {
            for (int col = x; col < x + width; col++) {
                cells.add(new Cell(col, row));
            }
        }

        for (CombatUnit unit : combatUnitService.findAll()) {
            for (Cell occupied : unit.getCells()) {
                for (Cell cell : cells) {
                    if (occupied.getX() == cell.getX()
                            && occupied.getY() == cell.getY()) {
                        throw new BusinessException(
                                "Клетка занята другой боевой единицей"
                        );
                    }
                }
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

        CombatUnit unit = new CombatUnit(cells, speed);
        unit.setAttackRange(attackRange);
        combatUnitService.add(unit);

        System.out.println("Боевая единица добавлена");
    }

    public void showAll() {
        List<CombatUnit> units = combatUnitService.findAll();

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

        CombatUnit unit = combatUnitService.findById(id);

        if (unit == null) {
            throw new CombatUnitNotFoundException(id);
        }

        System.out.print("Введите новую скорость: ");
        int speed = scanner.nextInt();

        if (speed < 0) {
            throw new BusinessException(
                    "Скорость не может быть отрицательной"
            );
        }

        unit.setSpeed(speed);
        combatUnitService.update(unit);

        System.out.println("Боевая единица обновлена");
    }

    public void remove() {
        System.out.print("Введите ID боевой единицы: ");
        int id = scanner.nextInt();

        if (!combatUnitService.remove(id)) {
            throw new CombatUnitNotFoundException(id);
        }

        System.out.println("Боевая единица удалена");
    }

    public void move() {
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            throw new BusinessException("Сначала создайте поле");
        }

        System.out.print("Введите ID юнита: ");
        int id = scanner.nextInt();

        CombatUnit unit = combatUnitService.findById(id);

        if (unit == null) {
            throw new CombatUnitNotFoundException(id);
        }

        if (unit.isDestroyed()) {
            throw new BusinessException(
                    "Уничтоженный юнит не может двигаться"
            );
        }

        if (unit.getSpeed() == 0) {
            throw new BusinessException("Этот юнит неподвижен");
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
            width = Math.max(
                    width,
                    cell.getX() - currentX + 1
            );
            height = Math.max(
                    height,
                    cell.getY() - currentY + 1
            );
        }

        System.out.print("Введите новую X: ");
        int x = scanner.nextInt();

        System.out.print("Введите новую Y: ");
        int y = scanner.nextInt();

        if (x < 1 || y < 1
                || x + width - 1 > battleField.getWidth()
                || y + height - 1 > battleField.getHeight()) {
            throw new BusinessException(
                    "Координаты за пределами поля"
            );
        }

        if (x != currentX && y != currentY) {
            throw new BusinessException(
                    "Двигаться можно только по горизонтали или вертикали"
            );
        }

        int dx = x - currentX;
        int dy = y - currentY;

        int distance = Math.abs(dx) + Math.abs(dy);

        if (distance == 0 || distance > unit.getSpeed()) {
            throw new BusinessException("Недопустимое расстояние");
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

                for (CombatUnit other : combatUnitService.findAll()) {
                    if (other.getId() == id) {
                        continue;
                    }

                    for (Cell cell : other.getCells()) {
                        if (cell.getX() == nextX
                                && cell.getY() == nextY) {
                            throw new BusinessException(
                                    "Путь занят другим юнитом"
                            );
                        }
                    }
                }

                for (StaticObject object : staticObjectService.findAll()) {
                    for (Cell cell : object.getCells()) {
                        if (cell.getX() == nextX
                                && cell.getY() == nextY) {
                            throw new BusinessException(
                                    "Путь заблокирован объектом"
                            );
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
        combatUnitService.update(unit);

        System.out.println("Юнит перемещён");
    }

    public void fire() {
        BattleField battleField = battleFieldService.get();

        if (battleField == null) {
            throw new BusinessException("Сначала создайте поле");
        }

        System.out.print("ID стреляющего юнита: ");
        int shooterId = scanner.nextInt();

        CombatUnit shooter = combatUnitService.findById(shooterId);

        if (shooter == null) {
            throw new CombatUnitNotFoundException(shooterId);
        }

        if (shooter.isDestroyed()) {
            throw new BusinessException(
                    "Уничтоженный юнит не может стрелять"
            );
        }

        System.out.println("Направление выстрела:");
        System.out.println("1 - вверх");
        System.out.println("2 - вниз");
        System.out.println("3 - влево");
        System.out.println("4 - вправо");
        System.out.print("Ваш выбор: ");

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
                throw new BusinessException(
                        "Неизвестное направление"
                );
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
                                "Снаряд попал в препятствие: "
                                        + object.getName()
                                        + " в клетке (" + x + ", " + y + ")"
                        );
                        return;
                    }
                }
            }

            for (CombatUnit target : combatUnitService.findAll()) {
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
                            "Здоровье: " + oldHealth
                                    + " -> " + target.getHealth()
                    );

                    if (target.isDestroyed()) {
                        combatUnitService.remove(target.getId());

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