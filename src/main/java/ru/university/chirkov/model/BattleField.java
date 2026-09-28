package ru.university.chirkov.model;

import java.util.List;

public class BattleField {

    private int width;
    private int height;
    private List<Cell> cells;

    public BattleField(int width, int height, List<Cell> cells) {
        this.width = width;
        this.height = height;
        this.cells = cells;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Cell> getCells() {
        return cells;
    }
}
