package ru.university.chirkov.ui;

import ru.university.chirkov.model.BattleField;
import ru.university.chirkov.model.Cell;
import ru.university.chirkov.model.CombatUnit;
import ru.university.chirkov.model.StaticObject;

import java.util.List;

public class ConsoleBattleFieldRenderer {

    public void render(
            BattleField battleField,
            List<CombatUnit> combatUnits,
            List<StaticObject> staticObjects) {

        int width = battleField.getWidth();
        int height = battleField.getHeight();

        System.out.println("Поле боя: " + width + "x" + height);

        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                char symbol = '.';

                for (CombatUnit combatUnit : combatUnits) {
                    for(Cell cell : combatUnit.getCells()) {
                        if (cell.getX() == x && cell.getY() == y) {
                            symbol = 'B';
                        }
                    }
                }

                for (StaticObject staticObject : staticObjects) {
                    for (Cell cell : staticObject.getCells()) {
                        if (cell.getX() == x && cell.getY() == y) {
                            symbol = '#';
                        }
                    }
                }

                System.out.print(symbol + " ");
            }

            System.out.println();
        }
    }
}