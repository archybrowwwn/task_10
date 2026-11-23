package ru.university.chirkov.repository;

import ru.university.chirkov.model.StaticObject;

import java.util.ArrayList;
import java.util.List;

public class StaticObjectRepositoryInMemoryImpl implements StaticObjectRepository {

    private List<StaticObject> objects = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void add(StaticObject object) {
        object.setId(nextId++);
        objects.add(object);
    }

    @Override
    public List<StaticObject> findAll() {
        return objects;
    }

    @Override
    public StaticObject findById(int id) {
        for (StaticObject object : objects) {
            if (object.getId() == id) {
                return object;
            }
        }
        return null;
    }

    @Override
    public boolean update(StaticObject object) {
        int index = objects.indexOf(findById(object.getId()));

        if (index == -1) {
            return false;
        }

        objects.set(index, object);
        return true;
    }

    @Override
    public boolean remove(int id) {
        return objects.remove(findById(id));
    }

    @Override
    public void deleteAll() {
        objects.clear();
    }
}
