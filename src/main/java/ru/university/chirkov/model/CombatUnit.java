package ru.university.chirkov.model;

import java.util.List;

public class CombatUnit {

    private List<Cell> cells;
    private int speed;

    public CombatUnit(List<Cell> cells, int speed) {
        this.cells = cells;
        this.speed = speed;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
