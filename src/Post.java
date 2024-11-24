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

    /*
    Returns a result to compare posts with each other using the number of likes they get, if both posts have the same
    number of likes, the one with a higher lexicographical score is considered larger.
     */
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
            // If both posts have the same number of likes use the builtin compareTo method for strings to get
            // a value conforming the reverse lexicographical order
            return this.id.compareTo(otherPost.id);
        }
    }

    public boolean like(String userId) {
        StringWrapper userSW = new StringWrapper(userId);
        // If this post is not liked by our user before
        if (!likedUsers.contains(userSW.id)) {
            numberOfLikes++;
            likedUsers.insert(userSW);

            return true;
        }

        // If the post is liked by our user before
        numberOfLikes--;
        likedUsers.remove(userSW);

        return false;
    }
}
