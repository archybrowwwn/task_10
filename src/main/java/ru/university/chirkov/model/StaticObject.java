package ru.university.chirkov.model;

import java.util.List;

public class StaticObject {

    private int id;
    private String name;
    private List<Cell> cells;

    public StaticObject(String name, List<Cell> cells) {
        this.name = name;
        this.cells = cells;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        return "Статический объект #" + id
                + ", название: " + name
                + ", клетки: " + cells;
    }
}
