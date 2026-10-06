package org.example;

public class MinHeap {
    private int[] heap;
    private int size;
    private final Metrics metrics;

    public MinHeap(Metrics metrics) {
        this.heap = new int[10];
        this.size = 0;
        this.metrics = metrics;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int[] newHeap = new int[heap.length * 2];
            for (int i = 0; i < size; i++) {
                metrics.steps++;
                metrics.moves++;
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }

    public void insert(int x) {
        ensureCapacity();
        heap[size] = x;
        metrics.moves++;
        siftUp(size);
        size++;
    }

    public int peekMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        return heap[0];
    }

    public int extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.steps++;
        int min = heap[0];
        heap[0] = heap[size - 1];
        metrics.moves++;
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return min;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            metrics.steps += 2;
            metrics.comparisons++;
            if (heap[index] < heap[parent]) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        while (2 * index + 1 < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = left;

            metrics.steps++;
            if (right < size) {
                metrics.steps++;
                metrics.comparisons++;
                if (heap[right] < heap[left]) {
                    smallest = right;
                }
            }

            metrics.steps += 2;
            metrics.comparisons++;
            if (heap[smallest] < heap[index]) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        metrics.moves += 3;
    }

    // BONUS B: O(n) Floyd's buildHeap
    public static MinHeap buildHeap(int[] array, Metrics metrics) {
        MinHeap minHeap = new MinHeap(metrics);
        minHeap.heap = new int[array.length];
        System.arraycopy(array, 0, minHeap.heap, 0, array.length);
        minHeap.size = array.length;
        metrics.moves += array.length;

        for (int i = (minHeap.size / 2) - 1; i >= 0; i--) {
            minHeap.siftDown(i);
        }
        return minHeap;
    }

    // Для валидации свойства кучи в тестах
    public boolean isValidHeap() {
        for (int i = 0; i < size; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && heap[i] > heap[left]) return false;
            if (right < size && heap[i] > heap[right]) return false;
        }
        return true;
    }
}