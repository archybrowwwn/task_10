package ru.university.chirkov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.university.chirkov.model.Cell;
import ru.university.chirkov.model.StaticObject;

import ru.university.chirkov.repository.StaticObjectRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class StaticObjectRepositoryTest {

    private StaticObjectRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StaticObjectRepository();
    }

    private StaticObject createObject(String name, int x, int y) {
        return new StaticObject(
                name,
                Collections.singletonList(new Cell(x, y))
        );
    }

    @Test
    void shouldAddObjectAndAssignId() {
        StaticObject object = createObject("Стена", 1, 1);

        repository.add(object);

        assertEquals(1, object.getId());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldFindObjectById() {
        StaticObject object = createObject("Стена", 2, 3);
        repository.add(object);

        StaticObject found = repository.findById(object.getId());

        assertNotNull(found);
        assertEquals(object.getId(), found.getId());
    }

    @Test
    void shouldReturnNullWhenObjectNotFound() {
        assertNull(repository.findById(999));
    }

    @Test
    void shouldUpdateObject() {
        StaticObject original = createObject("Стена", 1, 1);
        repository.add(original);

        StaticObject updatedObject = createObject("Здание", 2, 2);
        updatedObject.setId(original.getId());

        assertTrue(repository.update(updatedObject));
        assertEquals("Здание",
                repository.findById(original.getId()).getName());
    }

    @Test
    void shouldReturnFalseWhenUpdatingMissingObject() {
        StaticObject object = createObject("Стена", 1, 1);
        object.setId(999);

        assertFalse(repository.update(object));
    }

    @Test
    void shouldRemoveObject() {
        StaticObject object = createObject("Стена", 1, 1);
        repository.add(object);

        assertTrue(repository.remove(object.getId()));
        assertNull(repository.findById(object.getId()));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnFalseWhenRemovingMissingObject() {
        assertFalse(repository.remove(999));
    }

    @Test
    void shouldDeleteAllObjects() {
        repository.add(createObject("Стена", 1, 1));
        repository.add(createObject("Здание", 2, 2));

        repository.deleteAll();

        assertTrue(repository.findAll().isEmpty());
    }
}