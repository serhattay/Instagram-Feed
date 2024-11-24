import java.util.Iterator;

public class MyHashTable<E extends Denominable> {
    // Keep your tableSize always prime to increase efficiency
    private int size;
    private static final int DEFAULT_TABLE_SIZE = 101;
    protected MyLinkedList<E>[] hashArray;

    // Max load factor is chosen as 1, because we used Separate Chaining (Open Hashing) to resolve collisions,
    // if we had used another method like probing, we would choose a factor a little smaller than 0.5 to utilize the
    // table effectively
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

    /*
    Hash function is separated as a function even though it is simple to ease maintenance, and bug fix
     */
    public int hash(String id) {
        // Hashcode might return - values, "+ tableSize" is used to prevent negative indices
        return (id.hashCode() % hashArray.length + hashArray.length) % hashArray.length;
    }

    // To get the object by its id
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

    // Checks load factor to understand if rehash is needed
    private void checkArraySpace() {
        double loadFactor = size / (double) hashArray.length;
        if (loadFactor >= MAX_LOAD_FACTOR) {
            rehash();
        }
    }

    public boolean remove(E element) {
        return remove(element.getId()) != null;
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
        return contains(element.getId());
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
        // The size of the hash array is retained as a design choice when emptying the hash table
        hashArray = (MyLinkedList<E>[]) new MyLinkedList[hashArray.length];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        MyLinkedList<E>[] oldHashArray = hashArray;

        // When doing rehashing nextPrime function is used to keep the hash array size always prime, and
        // we look for the smallest larger prime after 2 * hash array length
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

    // Find the smallest prime integer larger than the given parameter x
    private int nextPrime(int x) {
        while (!isPrime(x)) {
            x++;
        }
        return x;
    }

    // Helper function for nextPrime to rapidly understand if the given parameter x is a prime
    private boolean isPrime(int x) {
        for (int i = 2; i <= Math.sqrt(x); i++) {
            if (x % i == 0) {
                return false;
            }
        }

        return true;
    }
}

/*
This wrapper class is used to support Strings in our Hash Table. Since this custom hash table only holds elements which
has an id for sure, all types that will be included needs to implement the Denominable interface. StringWrapper allows
us to hold Strings in the hash table by ensuring that they have an id; hence, our functions can use .id for the objects
safely.
 */
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

    // Even though this toString() method does the same job with getId(), in some parts of the code .id is more readable
    // and in the others .toString() increases readability.
    public String toString() {
        return id;
    }
}