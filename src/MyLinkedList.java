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

    public boolean contains(String id) {
        for (E element : this) {
            if (element.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // Removing an element by traversing the LL just one time using the iterator, iterator's delete method is used
    // to prevent illegitimate removals
    public E remove(String id) {
        Iterator<E> linkedListIterator = this.iterator();
        E nextElement = linkedListIterator.next();
        while(linkedListIterator.hasNext() && !nextElement.getId().equals(id)) {
            nextElement = linkedListIterator.next();
        }

        // size-- is included in the iterator's remove method
        linkedListIterator.remove();

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

    /*
    Fundamental node class for the linked list which holds an element, and a link to the next node to hold the linked
    structure
     */
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

    /*
    Custom iterator class for the custom linked list to power the for each loop and allow removals while traversing
    // using the .remove() method of the iterator
     */
    private class LinkedListIterator implements Iterator<E> {
        // Previous node of the last returned node
        private Node<E> prev;

        // Last returned node
        private Node<E> lastReturned;

        // Node to return when .next() is called
        private Node<E> current;


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

            // Shift all nodes by one node to the right
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
            size--;
        }
    }
}

