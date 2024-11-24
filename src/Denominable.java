/*
A very basic but fundamental interface for most of our elements to make sure that they have an id, and they implement
the getId() function. It is not possible to hold and object but hash it according to its id in design without an
interface or abstract class that guarantees that object has an id. Of course, this is not the only way to solve this
problem. For example, a hash table which takes <String, ObjectType> as types is a feasible and easy solution, but
in this implementation I have used the interface approach to practice marker interfaces in use. Both approaches
solve the problem effectively so it is a matter of design.
 */
public interface Denominable {
    String getId();
}
