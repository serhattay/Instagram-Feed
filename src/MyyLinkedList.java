public class MyyLinkedList<E> {
    public int size;
    private Node<E> head;
    private Node<E> tail;

    public int size() {
        return size;
    }

    MyyLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    MyyLinkedList(E element) {
        head = null;
        tail = null;
        size = 0;
        add(element);
    }

    MyyLinkedList(E[] elements) {
        head = null;
        tail = null;
        size = 0;
        for (E element : elements) {
            add(element);
        }
    }

    public int indexOf(E elementForIndexOf) {
        if (head == null) {
            return -1;
        }

        Node<E> currentNode = head;
        int idxCounter = 0;

        while(currentNode != null && !currentNode.element.equals(elementForIndexOf)) {
            currentNode = currentNode.next;
            idxCounter += 1;
        }

        if (currentNode != null) {
            return idxCounter;
        } else {
            return -1;
        }
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    protected static class Node<E> {
        E element;
        Node<E> next;

        Node(E element) {
            this.element = element;
            this.next = null;
        }

        public Node<E> getNext() {
            return next;
        }
    }

    public void add(E element) {
        Node<E> nodeToAdd = new Node<>(element);
        if (isEmpty()) {
            head = nodeToAdd;
            tail = nodeToAdd;
        } else {
            tail.next = nodeToAdd;
            tail = tail.next;
        }

        size++;
    }

    public void add(E element, int index) {
        // Check if the index is within the valid range
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // Create a new node with the specified element
        Node<E> newNode = new Node<>(element);

        if (index == 0) {
            // Inserting at the head (beginning) of the list
            newNode.next = head;
            head = newNode;
        } else {
            // Traverse to the node just before the desired index
            Node<E> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }

            // Insert the new node by adjusting the pointers
            newNode.next = current.next;
            current.next = newNode;

            if (size == index) {
                tail = newNode;
            }
        }

        // Increment the size of the list
        size++;
    }

    public E remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index is larger than the size of the linked list.");
        }

        Node<E> prevNode = null;
        Node<E> currentNode = head;
        for(int i = 0; i < index; i++) {
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
        // Now the currentNode is the node to remove

        E deletedItem = currentNode.element;

        if (prevNode != null) {
            prevNode.next = currentNode.next;
        } else {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        }

        size--;

        return deletedItem;

    }
    public boolean remove(E element) {
        Node<E> currentNode = head;
        Node<E> prevNode = null;
        while (currentNode != null && !currentNode.element.equals(element)) {
            prevNode = currentNode;
            currentNode = currentNode.next;
        }

        // This means currentNode.element == element
        if (currentNode != null) {
            // If the element is not the head
            if (prevNode != null) {
                prevNode.next = currentNode.next;
            } else { // If the element is the head
                head = head.next;
            }

            // If the element is the tail
            if (currentNode.next == null) {
                tail = prevNode;
            }

            size--;
            return true;
        }

        return false;
    }

    public boolean contains(E element) {
        Node<E> currentNode = head;
        while (currentNode != null && !currentNode.element.equals(element)) {
            currentNode = currentNode.next;
        }

        return currentNode != null;
    }

    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        Node<E> currentNode = head;
        for(int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        return currentNode.element;
    }



    public boolean isEmpty() {
        return size == 0;
    }
}
