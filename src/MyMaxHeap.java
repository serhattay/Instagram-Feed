public class MyMaxHeap<E extends Comparable<E>> {
    E[] heapArray;
    int currentSize;
    int capacity;

    // This choice of default capacity is quite arbitrary, it is not necessary for capacity to be prime
    private static final int DEFAULT_CAPACITY = 17;

    @SuppressWarnings("unchecked")
    MyMaxHeap() {
        heapArray = (E[]) new Comparable[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        currentSize = 0;
    }

    @SuppressWarnings("unchecked")
    MyMaxHeap(int capacity) {
        heapArray = (E[]) new Comparable[capacity];
        currentSize = 0;
        this.capacity = capacity;
    }

    @SuppressWarnings("unchecked")
    MyMaxHeap(E[] elements) {
        heapArray = (E[]) new Comparable[DEFAULT_CAPACITY];
        currentSize = 0;
        capacity = DEFAULT_CAPACITY;
        for (E element : elements) {
            insert(element);
        }
    }

    public void insert(E element) {
        checkArraySize();

        heapArray[++currentSize] = element;
        percolateUp(currentSize);
    }

    @SuppressWarnings("unchecked")
    private void checkArraySize() {
        if (currentSize >= capacity - 1) {
            E[] tempArray = (E[]) new Comparable[capacity * 2 + 1];
            System.arraycopy(heapArray, 1, tempArray, 1, currentSize);
            heapArray = tempArray;
            capacity = heapArray.length;
        }
    }

    private void percolateUp(int index) {
        E element = heapArray[index];
        int hole = index;

        for (heapArray[0] = element; element.compareTo(heapArray[hole / 2]) > 0; hole /= 2) {
            heapArray[hole] = heapArray[hole / 2];
        }
        heapArray[hole] = element;
    }

    /*
    Returns the largest item in the heap but does not change the heap.
     */
    public E getMax() {
        if (isEmpty()) {
            return null;
        }

        return heapArray[1];
    }

    /*
    Returns and deletes the largest item in the heap and uses percolate down to keep the heap's order and structural
    property.
     */
    public E extractMax() {
        if (isEmpty()) {
            return null;
        }

        E maxItem = getMax();
        heapArray[1] = heapArray[currentSize--];

        percolateDown(1);

        return maxItem;
    }

    private boolean isEmpty() {
        return currentSize == 0;
    }

    public void percolateDown(int index) {
        int child;
        // It is possible to take hole directly from the parameter but this assignment
        // increases understandability
        int hole = index;

        E temp = heapArray[hole];

        for ( ; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            // If the right child is larger add 1 to child index
            if (child != currentSize && heapArray[child + 1].compareTo(heapArray[child]) > 0) {
                child ++;
            }
            // Compare the largest child with the element
            if (heapArray[child].compareTo(temp) > 0) {
                heapArray[hole] = heapArray[child];
            } else {
                break;
            }
        }

        heapArray[hole] = temp;
    }

    // Creates heap from any array, it doesn't need be sorted
    public void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    @SuppressWarnings("unchecked")
    public void makeEmpty() {
        heapArray = (E[]) new Comparable[currentSize];
    }
}
