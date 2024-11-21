public class Instagram {
    // The two HashTables below hold IDs only
    private static MyHashTable<User> allUsers = new MyHashTable<>();
    private static MyHashTable<Post> allPosts = new MyHashTable<>();

    public static boolean doesUserExist(User user) {
        return doesUserExist(user.id);
    }

    public static boolean doesUserExist(String id) {
        return allUsers.contains(id);
    }

    public static boolean doesPostExist(Post post) {
        return allPosts.contains(post.id);
    }

    public static boolean doesPostExist(String id) {
        return allPosts.contains(id);
    }

    public static void addUser(User user) {
        allUsers.insert(user);
    }

    public static void addPost(Post post) {
        allPosts.insert(post);
    }

    public static User getUserObject(String userId) {
        return allUsers.getObject(userId);
    }

    public static Post getPostObject(String postId) {
        return allPosts.getObject(postId);
    }
}
