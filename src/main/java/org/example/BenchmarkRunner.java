package org.example;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int RUNS = 5;

    public static void main(String[] args) {
        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter("results/results.csv")) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                System.out.println("Running benchmark for n = " + n + "...");
                runW1(writer, n);
                runW2(writer, n);
                runW3(writer, n, "head");
                runW3(writer, n, "middle");
                runW4(writer, n);
            }
            System.out.println("Benchmark finished! Results saved to results/results.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runW1(PrintWriter writer, int n) {
        int[] arr = generateRandomArray(n);

        // DynamicArray
        long[] times = new long[RUNS];
        Metrics metrics = new Metrics();
        for (int r = 0; r < RUNS; r++) {
            Metrics tempM = new Metrics();
            DynamicArray da = new DynamicArray(tempM);
            for (int x : arr) da.add(x);

            Random rnd = new Random(42);
            long start = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                da.get(rnd.nextInt(n));
            }
            times[r] = System.nanoTime() - start;
            if (r == RUNS - 1) metrics = tempM;
        }
        writeCsvRow(writer, "W1", "-", "DynamicArray", n, getMedian(times), metrics);

        // MyLinkedList
        for (int r = 0; r < RUNS; r++) {
            Metrics tempM = new Metrics();
            MyLinkedList list = new MyLinkedList(tempM);
            for (int x : arr) list.add(x);

            Random rnd = new Random(42);
            long start = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
                list.get(rnd.nextInt(n));
            }
            times[r] = System.nanoTime() - start;
            if (r == RUNS - 1) metrics = tempM;
        }
        writeCsvRow(writer, "W1", "-", "MyLinkedList", n, getMedian(times), metrics);
    }

    private static void runW2(PrintWriter writer, int n) {
        int[] arr = generateRandomArray(n);
        int[] searchQueries = new int[1000];
        Random rnd = new Random(42);
        for (int i = 0; i < 500; i++) searchQueries[i] = arr[rnd.nextInt(n)];
        for (int i = 500; i < 1000; i++) searchQueries[i] = -rnd.nextInt(1000000) - 1;

        // DynamicArray
        long[] times = new long[RUNS];
        Metrics metrics = new Metrics();
        for (int r = 0; r < RUNS; r++) {
            Metrics tempM = new Metrics();
            DynamicArray da = new DynamicArray(tempM);
            for (int x : arr) da.add(x);

            tempM.reset();
            long start = System.nanoTime();
            for (int q : searchQueries) da.contains(q);
            times[r] = System.nanoTime() - start;
            if (r == RUNS - 1) metrics = tempM;
        }
        writeCsvRow(writer, "W2", "-", "DynamicArray", n, getMedian(times), metrics);

        // MyLinkedList
        for (int r = 0; r < RUNS; r++) {
            Metrics tempM = new Metrics();
            MyLinkedList list = new MyLinkedList(tempM);
            for (int x : arr) list.add(x);

            tempM.reset();
            long start = System.nanoTime();
            for (int q : searchQueries) list.contains(q);
            times[r] = System.nanoTime() - start;
            if (r == RUNS - 1) metrics = tempM;
        }
        writeCsvRow(writer, "W2", "-", "MyLinkedList", n, getMedian(times), metrics);
    }

    private static void runW3(PrintWriter writer, int n, String variant) {
        int[] arr = generateRandomArray(n);
        int targetIndex = variant.equals("head") ? 0 : n / 2;

        // DynamicArray
        long[] times = new long[RUNS];
        Metrics metrics = new Metrics();
        for (int r = 0; r < RUNS; r++) {
            Metrics tempM = new Metrics();
            DynamicArray da = new DynamicArray(tempM);
            for (int x : arr) da.add(x);

            tempM.reset();
            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) da.add(targetIndex, 999);
            for (int i = 0; i < 1000; i++) da.remove(targetIndex);
            times[r] = System.nanoTime() - start;
            if (r == RUNS - 1) metrics = tempM;
        }
        writeCsvRow(writer, "W3", variant, "DynamicArray", n, getMedian(times), metrics);

        // MyLinkedList
        for (int r = 0; r < RUNS; r++) {
            Metrics tempM = new Metrics();
            MyLinkedList list = new MyLinkedList(tempM);
            for (int x : arr) list.add(x);

            tempM.reset();
            long start = System.nanoTime();
            for (int i = 0; i < 1000; i++) list.add(targetIndex, 999);
            for (int i = 0; i < 1000; i++) list.remove(targetIndex);
            times[r] = System.nanoTime() - start;
            if (r == RUNS - 1) metrics = tempM;
        }
        writeCsvRow(writer, "W3", variant, "MyLinkedList", n, getMedian(times), metrics);
    }

    private static void runW4(PrintWriter writer, int n) {
        int[] arr = generateRandomArray(n);
        long[] times = new long[RUNS];
        Metrics metrics = new Metrics();

        for (int r = 0; r < RUNS; r++) {
            Metrics tempM = new Metrics();
            MinHeap heap = new MinHeap(tempM);

            long start = System.nanoTime();
            for (int x : arr) heap.insert(x);
            for (int i = 0; i < n; i++) heap.extractMin();
            times[r] = System.nanoTime() - start;
            if (r == RUNS - 1) metrics = tempM;
        }
        writeCsvRow(writer, "W4", "-", "MinHeap", n, getMedian(times), metrics);
    }

    private static int[] generateRandomArray(int n) {
        Random rnd = new Random(42);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(1000000);
        return arr;
    }

    private static double getMedian(long[] times) {
        Arrays.sort(times);
        return (times[2] / 1000000.0); // Перевод в миллисекунды
    }

    private static void writeCsvRow(PrintWriter writer, String w, String var, String struct, int n, double timeMs, Metrics m) {
        writer.printf("%s,%s,%s,%d,%.4f,%d,%d,%d\n", w, var, struct, n, timeMs, m.steps, m.moves, m.comparisons);
    }
}