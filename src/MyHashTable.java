import java.util.Iterator;
import java.util.LinkedList;

public class MyHashTable<E extends Denominable> {
    // Keep your tableSize always prime
    private int size;
    private static final int DEFAULT_TABLE_SIZE = 101;
    private MyLinkedList<E>[] hashArray;

    private static final double MAX_LOAD_FACTOR = 1;


    public MyHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    @SuppressWarnings("unchecked")
    public MyHashTable(int capacity) {
        size = 0;
        hashArray = (MyLinkedList<E>[]) new MyLinkedList[nextPrime(capacity)];
    }

    public int hash(E element) {
        // Hashcode might return - values, "+ tableSize" is used to prevent negative indices
        return hash(element.getId());
    }

    public int hash(String id) {
        // Hashcode might return - values, "+ tableSize" is used to prevent negative indices
        return (id.hashCode() % hashArray.length + hashArray.length) % hashArray.length;
    }

    public E getObject(String id) {
        int hashVal = hash(id);
        MyLinkedList<E> chainingArray = hashArray[hashVal];

        for (E element : chainingArray) {
            if (element.getId().equals(id)) {
                return element;
            }
        }

        return null;
    }

    public void insert(E element) {
        checkArraySpace();

        int hashValue = hash(element);
        if (hashArray[hashValue] == null) {
            hashArray[hashValue] = new MyLinkedList<>();
        }
        hashArray[hashValue].add(element);

        size++;
    }

    private void checkArraySpace() {
        double loadFactor = size / (double) hashArray.length;
        if (loadFactor >= MAX_LOAD_FACTOR) {
            rehash();
        }
    }

    public boolean remove(E element) {
        int hashValue = hash(element);
        if (hashArray[hashValue] != null) {
            if(hashArray[hashValue].remove(element)) {
                size--;
                return true;
            }
        }

        return false;
    }

    public E remove(String id) {
        int hashValue = hash(id);
        E element;
        if (hashArray[hashValue] != null) {
            if((element = hashArray[hashValue].remove(id)) != null) {
                size--;
                return element;
            }
        }

        return null;
    }

    public boolean contains(E element) {
        int hashValue = hash(element);
        if (hashArray[hashValue] != null) {
            return hashArray[hashValue].contains(element);
        }

        return false;
    }

    public boolean contains(String id) {
        int hashValue = hash(id);
        if (hashArray[hashValue] != null) {
            return hashArray[hashValue].contains(id);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public void makeEmpty() {
        hashArray = (MyLinkedList<E>[]) new MyLinkedList[hashArray.length];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        MyLinkedList<E>[] oldHashArray = hashArray;
        hashArray = new MyLinkedList[nextPrime(2 * hashArray.length)];


        // Transferring all the elements from the smaller hash table to the bigger one
        for (MyLinkedList<E> currentLinkedList : oldHashArray) {

            E currentNode;
            Iterator<E> myIterator;
            if (currentLinkedList != null) {
                myIterator = currentLinkedList.iterator();
            } else {
                continue;
            }
            while (myIterator.hasNext()) {
                currentNode = myIterator.next();
                int hashValue = hash(currentNode);
                if (hashArray[hashValue] == null) {
                    hashArray[hashValue] = new MyLinkedList<>();
                }
                hashArray[hashValue].add(currentNode);
            }
        }
    }

    private int nextPrime(int x) {
        while (!isPrime(x)) {
            x++;
        }
        return x;
    }

    private boolean isPrime(int x) {
        for (int i = 2; i <= Math.sqrt(x); i++) {
            if (x % i == 0) {
                return false;
            }
        }

        return true;
    }
}

class StringWrapper implements Denominable {
    String id;
    StringWrapper() {
        id = null;
    }

    StringWrapper(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
