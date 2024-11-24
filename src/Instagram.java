import java.util.ArrayList;

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

        User user = allUsers.getObject(userId);

        for (MyLinkedList<StringWrapper> followedUsers : user.following.hashArray) {
            if (followedUsers != null) {
                for (StringWrapper followedUserId : followedUsers) {
                    User followedUser = allUsers.getObject(followedUserId.getId());

                    for (MyLinkedList<StringWrapper> postsLinkedList : followedUser.posts.hashArray) {
                        if (postsLinkedList != null) {
                            for (StringWrapper postId : postsLinkedList) {
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

        for (int i = 0; i < feedArrayList.size(); i++) {
            currentPost = feedArrayList.get(i);
            sb.append(userId).append(" saw ").append(currentPost.id).append(" while scrolling");
            if (isLikedArray[i] == 1) {
                currentPost.like(userId);
                sb.append(" and clicked the like button");
            }

            sb.append(".");

            if (i != isLikedArray.length - 1) {
                sb.append("\n");
            }

            user.seenPosts.insert(new StringWrapper(currentPost.id));
        }

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
