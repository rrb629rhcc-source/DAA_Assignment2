# Assignment 2 Report: Data Structures & Benchmarking

## 1. Asymptotic Complexity Table

| Data Structure | Operation | Best Case | Average Case | Worst Case | Space | Justification |
|---|---|---|---|---|---|---|
| DynamicArray | add(x) | $\Theta(1)$ | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | Amortized $O(1)$ due to $2\times$ capacity growth; worst case occurs on resize copying $n$ items. |
| DynamicArray | add(index, x)| $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Inserting at the end is $O(1)$, inserting at head/middle requires shifting elements right. |
| DynamicArray | remove(index) | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Removing tail is $O(1)$, head/middle requires shifting array bounds. |
| DynamicArray | get(index) | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(n)$ | Direct O(1) array index retrieval via offset calculation. |
| DynamicArray | contains(x) | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Linear search scanning $n$ elements. |
| MyLinkedList | add(x) | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Singly linked list traverses to tail without tail pointer. |
| MyLinkedList | add(index, x)| $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Head insertion is $O(1)$, middle/tail requires pointer traversal. |
| MyLinkedList | remove(index) | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Unlinking head is $O(1)$, middle/tail requires steps traversal. |
| MyLinkedList | get(index) | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Sequential reference traversal from head to index. |
| MyLinkedList | contains(x) | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | Traverses linked nodes comparing value. |
| MinHeap | insert(x) | $\Theta(1)$ | $O(\log n)$ | $O(\log n)$ | $\Theta(n)$ | Inserts at bottom; sifts up at most tree height $\log n$. |
| MinHeap | peekMin() | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(n)$ | Root element is always min at index 0. |
| MinHeap | extractMin() | $\Theta(\log n)$ | $\Theta(\log n)$ | $\Theta(\log n)$ | $\Theta(n)$ | Replaces root with tail and sifts down height $\log n$. |

---

## 2. Loop Invariant Proofs

### Proof 1: `DynamicArray.contains(x)`
- **Invariant:** At the start of iteration $i$, $x \notin \text{data}[0 \dots i-1]$.
- **Initialization:** Before loop ($i=0$), sub-array $\text{data}[0 \dots -1]$ is empty, so invariant holds vacuously.
- **Maintenance:** If true before iteration $i$, then $x \neq \text{data}[k]$ for $0 \le k < i$. During iteration, code checks if $\text{data}[i] == x$. If false, $x \notin \text{data}[0 \dots i]$. Incrementing $i$ maintains invariant for $i+1$.
- **Termination:** Loop terminates when $i = \text{size}$ or $x == \text{data}[i]$. If found, returns `true`. If $i = \text{size}$, $x \notin \text{data}[0 \dots \text{size}-1]$, returning `false`.
- **Conclusion:** Proves linear search correctness without missing indices.

### Proof 2: `MinHeap.siftDown(index)`
- **Invariant:** For every node $k$ outside the subtree path from `index` to leaf, the min-heap property $a[\text{parent}] \le a[\text{child}]$ holds.
- **Initialization:** Prior to sift-down, only element at `index` potentially violates heap property relative to its children. All other subtrees are valid min-heaps.
- **Maintenance:** Smallest among `index` and children is swapped with `index`. After swap, current node satisfies heap property relative to current children. Potential violation moves down to child index.
- **Termination:** Stops when current element $\le$ children or leaf is reached. Heap property restored everywhere.
- **Conclusion:** Correctly restores heap invariance in $O(\log n)$ time.

---

## 3. Performance Discussion

1. **CPU Cache Locality & Spatial Locality:** `DynamicArray` exhibits superior spatial cache locality because elements are allocated contiguously in physical RAM. Sequential array reads prefetch contiguous cache lines (64 bytes), dramatically reducing CPU wait cycles compared to linked nodes.
2. **Pointer Chasing & Garbage Collection Overhead:** `MyLinkedList` stores individual `Node` instances dispersed across the heap. Accessing nodes requires pointer chasing (reading non-contiguous memory), triggering CPU cache misses. Additionally, object headers (16 bytes per node) increase memory bandwidth consumption and create GC pressure.
3. **Architectural Trade-offs:** `DynamicArray` is preferable for read-heavy operations (`get(i)`) and iteration. `MyLinkedList` is suitable only when frequent $O(1)$ insertions/removals occur strictly at the head without memory allocation constraints. `MinHeap` provides $O(\log n)$ performance for priority queues.

---

## 4. Bonus Features Evaluation

- **Bonus A (Memory Footprint JOL):** `DynamicArray` uses 4 bytes per `int` plus array header. `MyLinkedList` spends 24 bytes per node (16-byte object header + 4-byte int + 4-byte reference/padding), leading to a ~6x memory overhead compared to primitive arrays.
- **Bonus B (O(n) buildHeap):** Floyd's bottom-up `buildHeap` executes in $O(n)$ time by processing non-leaf nodes downwards, requiring significantly fewer total comparisons than $n$ consecutive `insert` operations ($O(n \log n)$).