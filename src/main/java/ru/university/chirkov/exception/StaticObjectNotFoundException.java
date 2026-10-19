package ru.university.chirkov.exception;

public class StaticObjectNotFoundException extends BusinessException {

    public StaticObjectNotFoundException(int id) {
        super("Статический объект с ID " + id + " не найден");
    }
}