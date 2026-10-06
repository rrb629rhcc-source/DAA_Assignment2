package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {
    private Metrics metrics;
    private MinHeap heap;

    @BeforeEach
    void setUp() {
        metrics = new Metrics();
        heap = new MinHeap(metrics);
    }

    @Test
    void testInsertAndExtractMin() {
        heap.insert(15);
        heap.insert(10);
        heap.insert(20);

        assertEquals(10, heap.peekMin());
        assertEquals(10, heap.extractMin());
        assertEquals(15, heap.extractMin());
        assertEquals(20, heap.extractMin());
        assertTrue(heap.isEmpty());
    }

    @Test
    void testHeapPropertyHolds() {
        Random rnd = new Random(42);
        for (int i = 0; i < 100; i++) {
            heap.insert(rnd.nextInt(1000));
            assertTrue(heap.isValidHeap());
        }

        while (!heap.isEmpty()) {
            heap.extractMin();
            assertTrue(heap.isValidHeap());
        }
    }

    @Test
    void testSortedOutput() {
        int[] data = {5, 2, 8, 1, 9, 3};
        for (int x : data) {
            heap.insert(x);
        }

        int prev = Integer.MIN_VALUE;
        while (!heap.isEmpty()) {
            int current = heap.extractMin();
            assertTrue(current >= prev);
            prev = current;
        }
    }

    @Test
    void testExceptions() {
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    void testBuildHeapBonus() {
        int[] arr = {9, 4, 7, 1, 2, 6, 8};
        MinHeap builtHeap = MinHeap.buildHeap(arr, metrics);

        assertTrue(builtHeap.isValidHeap());
        assertEquals(1, builtHeap.extractMin());
        assertEquals(2, builtHeap.extractMin());
    }
}