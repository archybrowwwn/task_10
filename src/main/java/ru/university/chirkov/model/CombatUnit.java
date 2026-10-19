package ru.university.chirkov.model;

import java.util.List;

public class CombatUnit {

    private int id;
    private List<Cell> cells;
    private int speed;
    private int health;
    private int attackRange;
    private int damage;

    public CombatUnit(List<Cell> cells, int speed) {
        this.cells = cells;
        this.speed = speed;
        this.health = 100;
        this.attackRange = 3;
        this.damage = 25;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, health);
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        if (attackRange < 1) {
            throw new IllegalArgumentException(
                    "Дальность стрельбы должна быть больше нуля"
            );
        }
        this.attackRange = attackRange;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException(
                    "Урон не может быть отрицательным"
            );
        }
        this.damage = damage;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    @Override
    public String toString() {
        return "Боевой юнит #" + id
                + ", скорость: " + speed
                + ", здоровье: " + health
                + ", дальность стрельбы: " + attackRange
                + ", урон: " + damage
                + ", клетки: " + cells;
    }
}
