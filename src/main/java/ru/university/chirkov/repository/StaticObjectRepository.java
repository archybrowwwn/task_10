package ru.university.chirkov.repository;

import ru.university.chirkov.model.StaticObject;

import java.util.ArrayList;
import java.util.List;

public class StaticObjectRepository {

    private List<StaticObject> objects = new ArrayList<>();

    public void add(StaticObject object) {
        objects.add(object);
    }

    public List<StaticObject> findAll() {
        return objects;
    }

    public void remove(StaticObject object) {
        objects.remove(object);
    }
}
