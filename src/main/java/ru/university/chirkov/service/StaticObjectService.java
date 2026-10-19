package ru.university.chirkov.service;

import ru.university.chirkov.model.StaticObject;
import ru.university.chirkov.repository.StaticObjectRepository;

import java.util.List;

public class StaticObjectService {

    private StaticObjectRepository repository;

    public StaticObjectService(StaticObjectRepository repository) {
        this.repository = repository;
    }

    public void add(StaticObject object) {
        repository.add(object);
    }

    public List<StaticObject> findAll() {
        return repository.findAll();
    }

    public StaticObject findById(int id) {
        return repository.findById(id);
    }

    public boolean update(StaticObject object) {
        return repository.update(object);
    }

    public boolean remove(int id) {
        return repository.remove(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
