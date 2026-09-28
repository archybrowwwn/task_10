package ru.university.chirkov.model;

import java.util.List;

public class StaticObject {

    private List<Cell> cells;

    public StaticObject(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
