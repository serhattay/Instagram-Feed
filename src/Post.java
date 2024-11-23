public class Post implements Denominable, Comparable<Post> {
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

    @Override
    public int compareTo(Post otherPost) {
        if (otherPost == null) {
            throw new NullPointerException("Post to compare is null");
        }

        if (this.numberOfLikes > otherPost.numberOfLikes) {
            return 1;
        } else if (this.numberOfLikes < otherPost.numberOfLikes) {
            return -1;
        } else {
            return this.id.compareTo(otherPost.id);
        }
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
