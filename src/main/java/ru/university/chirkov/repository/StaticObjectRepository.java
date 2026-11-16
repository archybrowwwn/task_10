package ru.university.chirkov.repository;

import ru.university.chirkov.model.StaticObject;
import java.util.List;

public interface StaticObjectRepository {
    void add(StaticObject object);
    List<StaticObject> findAll();
    StaticObject findById(int id);
    boolean update(StaticObject object);
    boolean remove(int id);
    void deleteAll();
}