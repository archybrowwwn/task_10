package ru.university.chirkov.exception;

public class CombatUnitNotFoundException extends BusinessException {

    public CombatUnitNotFoundException(int id) {
        super("Боевая единица с ID " + id + " не найдена");
    }
}