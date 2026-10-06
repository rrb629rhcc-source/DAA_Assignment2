package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private Metrics metrics;
    private MyLinkedList list;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();
        list = new MyLinkedList(metrics);
    }

    @Test
    void testAddAndGet() {
        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testAddAtIndex() {
        list.add(10);
        list.add(30);
        list.add(1, 20); // Вставка в середину

        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void testRemove() {
        list.add(10);
        list.add(20);
        list.add(30);

        int removed = list.remove(1);

        assertEquals(20, removed);
        assertEquals(2, list.size());
        assertEquals(30, list.get(1));
    }

    @Test
    void testContains() {
        list.add(5);
        list.add(15);

        assertTrue(list.contains(5));
        assertFalse(list.contains(99));
    }

    @Test
    void testIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, 100));
    }
}