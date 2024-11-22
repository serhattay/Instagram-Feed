import java.util.ArrayList;
public class User implements Denominable {
    String id;
    MyHashTable<StringWrapper> following;
    MyHashTable<StringWrapper> posts;
    MyHashTable<StringWrapper> seenPosts;

    User() {
        this(null);
    }

    User(String id) {
        this.id = id;
        following = new MyHashTable<>();
        posts = new MyHashTable<>();
        seenPosts = new MyHashTable<>();
    }


    @Override
    public String getId() {
        return id;
    }
}
