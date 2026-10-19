package ru.university.chirkov.exception;

public class StaticObjectNameException extends BusinessException {

    public StaticObjectNameException() {
        super("Название статического объекта не может быть пустым");
    }
}