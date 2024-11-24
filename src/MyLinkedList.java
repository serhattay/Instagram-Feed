import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E extends Denominable> implements Iterable<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    MyLinkedList(E[] elements) {
        this();
        for (E element : elements) {
            add(element);
        }
    }

    // Works well, tested
    public boolean contains(String id) {
        for (E element : this) {
            if (element.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // Works well, tested
    public E remove(String id) {
        Iterator<E> linkedListIterator = this.iterator();
        E nextElement = linkedListIterator.next();
        while(linkedListIterator.hasNext() && !nextElement.getId().equals(id)) {
            nextElement = linkedListIterator.next();
        }

        linkedListIterator.remove();

        size--;

        return nextElement;
    }

    public void add(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = tail.next;
        }

        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private static class Node<E> {
        E element;
        Node<E> next;

        Node() {
            this(null, null);
        }

        Node(E element) {
            this.element = element;
        }

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }
    private class LinkedListIterator implements Iterator<E> {
        private Node<E> prev;
        private Node<E> current;
        private Node<E> lastReturned;

        LinkedListIterator() {
            prev = null;
            current = head;
            lastReturned = null;
        }
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No element is left.");
            }

            prev = lastReturned;
            lastReturned = current;
            current = current.next;
            return lastReturned.element;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("There is no last returned element to remove.");
            }

            // Removing the head
            if (prev == null) {
                head = current;
            } else { // Removing an element except head
                if (lastReturned == tail) { // Removing the tail
                    tail = prev;
                }

                prev.next = current;
            }

            lastReturned = null;
        }
    }
}

