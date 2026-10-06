package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {
    private Metrics metrics;
    private DynamicArray array;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();
        array = new DynamicArray(metrics);
    }

    @Test
    void testAddAndGet() {
        array.add(10);
        array.add(20);
        array.add(30);

        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testAddAtIndex() {
        array.add(10);
        array.add(30);
        array.add(1, 20); // Вставка в середину

        assertEquals(3, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
    }

    @Test
    void testRemove() {
        array.add(10);
        array.add(20);
        array.add(30);

        int removed = array.remove(1);

        assertEquals(20, removed);
        assertEquals(2, array.size());
        assertEquals(30, array.get(1));
    }

    @Test
    void testContains() {
        array.add(5);
        array.add(15);

        assertTrue(array.contains(5));
        assertFalse(array.contains(99));
    }

    @Test
    void testIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(5, 100));
    }

    @Test
    void testResizing() {
        for (int i = 0; i < 25; i++) {
            array.add(i);
        }
        assertEquals(25, array.size());
        assertEquals(24, array.get(24));
    }
}