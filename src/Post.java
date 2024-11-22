public class Post implements Denominable {
    String id;
    int numberOfLikes;

    MyHashTable<StringWrapper> likedUsers;

    Post() {
        this(null, 0);
    }

    Post(String id) {
        this(id, 0);
    }
    Post(String id, int numberOfLikes) {
        this.id = id;
        this.numberOfLikes = numberOfLikes;
        likedUsers = new MyHashTable<>();
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
