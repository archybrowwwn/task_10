package ru.university.chirkov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.university.chirkov.model.BattleField;
import ru.university.chirkov.model.Cell;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BattleFieldRepositoryTest {

    private BattleFieldRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BattleFieldRepository();
    }

    private BattleField createBattleField(int width, int height) {
        return new BattleField(
                width,
                height,
                Collections.singletonList(new Cell(0, 0))
        );
    }

    @Test
    void shouldSaveBattleField() {
        BattleField field = createBattleField(10, 10);

        repository.save(field);

        assertSame(field, repository.get());
    }

    @Test
    void shouldReturnNullWhenBattleFieldNotSaved() {
        assertNull(repository.get());
    }

    @Test
    void shouldUpdateExistingBattleField() {
        repository.save(createBattleField(10, 10));
        BattleField updated = createBattleField(20, 15);

        boolean result = repository.update(updated);

        assertTrue(result);
        assertSame(updated, repository.get());
        assertEquals(20, repository.get().getWidth());
        assertEquals(15, repository.get().getHeight());
    }

    @Test
    void shouldNotUpdateWhenBattleFieldDoesNotExist() {
        BattleField field = createBattleField(20, 15);

        boolean result = repository.update(field);

        assertFalse(result);
        assertNull(repository.get());
    }

    @Test
    void shouldDeleteExistingBattleField() {
        repository.save(createBattleField(10, 10));

        boolean result = repository.delete();

        assertTrue(result);
        assertNull(repository.get());
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingBattleField() {
        boolean result = repository.delete();

        assertFalse(result);
        assertNull(repository.get());
    }
}