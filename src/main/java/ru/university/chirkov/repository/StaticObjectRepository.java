package ru.university.chirkov.repository;

import ru.university.chirkov.model.StaticObject;

import java.util.ArrayList;
import java.util.List;

public class StaticObjectRepository {

    private List<StaticObject> objects = new ArrayList<>();
    private int nextId = 1;

    public void add(StaticObject object) {
        object.setId(nextId++);
        objects.add(object);
    }

    public List<StaticObject> findAll() {
        return objects;
    }

    public StaticObject findById(int id) {
        for (StaticObject object : objects) {
            if (object.getId() == id) {
                return object;
            }
        }
        return null;
    }

    public boolean update(StaticObject object) {
        int index = objects.indexOf(findById(object.getId()));

        if (index == -1) {
            return false;
        }

        objects.set(index, object);
        return true;
    }

    public boolean remove(int id) {
        return objects.remove(findById(id));
    }
}
