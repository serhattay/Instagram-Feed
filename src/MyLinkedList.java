import java.util.Iterator;
import java.util.LinkedList;

public class MyLinkedList<E extends Denominable> extends LinkedList<E> {
    public boolean contains(String id) {
        for (E element : this) {
            if (element.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public E remove(String id) {
        Iterator<E> linkedListIterator = this.iterator();
        E nextElement = linkedListIterator.next();
        while(linkedListIterator.hasNext() && !nextElement.getId().equals(id)) {
            nextElement = linkedListIterator.next();
        }

        linkedListIterator.remove();

        return nextElement;
    }
}