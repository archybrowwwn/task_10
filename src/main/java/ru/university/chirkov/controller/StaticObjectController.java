package ru.university.chirkov.controller;

import ru.university.chirkov.model.StaticObject;
import ru.university.chirkov.service.StaticObjectService;

import java.util.List;

public class StaticObjectController {

    private StaticObjectService service;

    public StaticObjectController(StaticObjectService service) {
        this.service = service;
    }

    public void add(StaticObject object) {
        service.add(object);
    }

    public List<StaticObject> getAll() {
        return service.findAll();
    }

    public void remove(StaticObject object) {
        service.remove(object);
    }
}
