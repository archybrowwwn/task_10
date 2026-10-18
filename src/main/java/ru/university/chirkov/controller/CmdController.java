package ru.university.chirkov.controller;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CmdController {

    private BattleFieldController battleFieldController;
    private CombatUnitController combatUnitController;
    private StaticObjectController staticObjectController;
    private Scanner scanner;

    public CmdController(
            BattleFieldController battleFieldController,
            CombatUnitController combatUnitController,
            StaticObjectController staticObjectController,
            Scanner scanner) {

        this.battleFieldController = battleFieldController;
        this.combatUnitController = combatUnitController;
        this.staticObjectController = staticObjectController;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("\n1. Создать поле");
            System.out.println("2. Показать поле");
            System.out.println("3. Изменить поле");
            System.out.println("4. Удалить поле");
            System.out.println("5. Добавить боевую единицу");
            System.out.println("6. Показать боевые единицы");
            System.out.println("7. Изменить боевую единицу");
            System.out.println("8. Удалить боевую единицу");
            System.out.println("9. Добавить статический объект");
            System.out.println("10. Показать статические объекты");
            System.out.println("11. Изменить статический объект");
            System.out.println("12. Удалить статический объект");
            System.out.println("13. Переместить боевую единицу");
            System.out.println("14. Выстрелить");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            try {
                if (!scanner.hasNextInt()) {
                    System.out.println("Введите число");
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                        continue;
                    }
                    return;
                }

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        battleFieldController.create();
                        break;
                    case 2:
                        battleFieldController.show();
                        break;
                    case 3:
                        battleFieldController.update();
                        break;
                    case 4:
                        battleFieldController.delete();
                        break;
                    case 5:
                        combatUnitController.add();
                        break;
                    case 6:
                        combatUnitController.showAll();
                        break;
                    case 7:
                        combatUnitController.update();
                        break;
                    case 8:
                        combatUnitController.remove();
                        break;
                    case 9:
                        staticObjectController.add();
                        break;
                    case 10:
                        staticObjectController.showAll();
                        break;
                    case 11:
                        staticObjectController.update();
                        break;
                    case 12:
                        staticObjectController.remove();
                        break;
                    case 13:
                        combatUnitController.move();
                        break;
                    case 14:
                        combatUnitController.fire();
                        break;
                    case 0:
                        System.out.println("Выход из программы.");
                        return;
                    default:
                        System.out.println("Неверный пункт меню");
                }

            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода. Введите число.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}