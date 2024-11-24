import java.util.ArrayList;

/*
A database that holds all users and posts, unlike the other classes that holds their ids as strings to use less space,
Instagram holds their objects because they need to be kept somewhere.
 */
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

    public static MyMaxHeap<Post> getPostHeapForFeed(String userId) {
        MyMaxHeap<Post> feedHeap = new MyMaxHeap<>();

        // The user which the feed is created for
        User user = allUsers.getObject(userId);

        // Traversing all the buckets of the hash table which stores users that our user follows
        for (MyLinkedList<StringWrapper> followedUsers : user.following.hashArray) {
            // Since we are traversing a hash table, some buckets might be null
            if (followedUsers != null) {
                // Traversing the linked list in the bucket
                for (StringWrapper followedUserId : followedUsers) {
                    // Get the user object to reach his/her posts
                    User followedUser = allUsers.getObject(followedUserId.getId());

                    // Traversing all buckets of the hash table holding this particular user's posts
                    for (MyLinkedList<StringWrapper> postsLinkedList : followedUser.posts.hashArray) {
                        // Since we are traversing a hash table, some buckets might be null
                        if (postsLinkedList != null) {
                            // Traversing the linked list in the bucket to reach all the posts that are hashed to the
                            // same hash code
                            for (StringWrapper postId : postsLinkedList) {
                                // If the post is not seen by our user add it to the feed heap to later sort
                                if (!user.seenPosts.contains(postId)) {
                                    Post post = allPosts.getObject(postId.toString());
                                    feedHeap.insert(post);
                                }
                            }
                        }
                    }
                }
            }
        }

        return feedHeap;
    }

    public static String scrollThroughFeed(String userId, int num, int[] isLikedArray) {
        if (!Instagram.doesUserExist(userId)) {
            return "Some error occurred in scroll_through_feed.";
        }

        User user = Instagram.getUserObject(userId);
        StringBuilder sb = new StringBuilder(userId).append(" is scrolling through feed:\n");
        ArrayList<Post> feedArrayList = Utility.generateFeedArrayList(userId, num);
        Post currentPost;

        // Creating the scroll through feed string
        for (int i = 0; i < feedArrayList.size(); i++) {
            currentPost = feedArrayList.get(i);
            sb.append(userId).append(" saw ").append(currentPost.id).append(" while scrolling");

            // If the post is liked
            if (isLikedArray[i] == 1) {
                currentPost.like(userId);
                sb.append(" and clicked the like button");
            }

            sb.append(".");

            // Remove the extra new line at the end
            if (i != isLikedArray.length - 1) {
                sb.append("\n");
            }

            user.seenPosts.insert(new StringWrapper(currentPost.id));
        }

        // In case of insufficient number of posts
        if (feedArrayList.size() < num) {
            sb.append("No more posts in feed.");
        }

        return sb.toString();
    }

    public static MyMaxHeap<Post> getMaxHeapOfUserPosts(String userId) {
        User user = Instagram.getUserObject(userId);

        Post post;
        MyMaxHeap<Post> postsHeap = new MyMaxHeap<>();
        for (MyLinkedList<StringWrapper> postsLinkedList : user.posts.hashArray) {
            if (postsLinkedList != null) {
                for (StringWrapper postId : postsLinkedList) {
                    post = allPosts.getObject(postId.toString());
                    postsHeap.insert(post);

                }
            }
        }

        return postsHeap;
    }
}
