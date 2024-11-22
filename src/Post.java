public class Post implements Denominable {
    String id;
    int numberOfLikes;

    String author;

    MyHashTable<StringWrapper> likedUsers;

    Post() {
        this(null);
    }

    Post(String id) {
        this(id, 0, null);
    }
    Post(String id, int numberOfLikes, String author) {
        this.id = id;
        this.numberOfLikes = numberOfLikes;
        likedUsers = new MyHashTable<>();
        this.author = author;
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean like(String userId) {
        StringWrapper userSW = new StringWrapper(userId);
        if (!likedUsers.contains(userSW.id)) {
            numberOfLikes++;
            likedUsers.insert(userSW);

            return true;
        }

        numberOfLikes--;
        likedUsers.remove(userSW);

        return false;
    }
}
