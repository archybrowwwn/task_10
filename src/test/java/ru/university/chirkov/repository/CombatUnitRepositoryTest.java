
package ru.university.chirkov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.university.chirkov.model.Cell;
import ru.university.chirkov.model.CombatUnit;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CombatUnitRepositoryTest {

    private CombatUnitRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CombatUnitRepository();
    }

    private CombatUnit createUnit(int x, int y, int speed) {
        return new CombatUnit(
                Collections.singletonList(new Cell(x, y)),
                speed
        );
    }

    @Test
    void shouldAddUnitAndAssignId() {
        CombatUnit unit = createUnit(1, 1, 3);

        repository.add(unit);

        assertEquals(1, unit.getId());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldFindUnitById() {
        CombatUnit unit = createUnit(2, 3, 4);
        repository.add(unit);

        CombatUnit found = repository.findById(unit.getId());

        assertNotNull(found);
        assertEquals(unit.getId(), found.getId());
    }

    @Test
    void shouldReturnNullWhenUnitNotFound() {
        assertNull(repository.findById(999));
    }

    @Test
    void shouldUpdateUnit() {
        CombatUnit original = createUnit(1, 1, 2);
        repository.add(original);

        CombatUnit updatedUnit = createUnit(2, 2, 5);
        updatedUnit.setId(original.getId());

        assertTrue(repository.update(updatedUnit));
        assertEquals(5,
                repository.findById(original.getId()).getSpeed());
    }

    @Test
    void shouldReturnFalseWhenUpdatingMissingUnit() {
        CombatUnit unit = createUnit(1, 1, 2);
        unit.setId(999);

        assertFalse(repository.update(unit));
    }

    @Test
    void shouldRemoveUnit() {
        CombatUnit unit = createUnit(1, 1, 2);
        repository.add(unit);

        assertTrue(repository.remove(unit.getId()));
        assertNull(repository.findById(unit.getId()));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnFalseWhenRemovingMissingUnit() {
        assertFalse(repository.remove(999));
    }

    @Test
    void shouldDeleteAllUnits() {
        repository.add(createUnit(1, 1, 2));
        repository.add(createUnit(3, 3, 4));

        repository.deleteAll();

        assertTrue(repository.findAll().isEmpty());
    }
}
