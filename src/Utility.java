import java.io.*;
import java.util.ArrayList;

/*
This Utility class is used for functionality, the main purpose is to write static functions somewhere else than Main.
 */
public class Utility {

    /*
    This function checks whether the input file exists and also makes sure that its type is a file
     */
    public static void checkDirectories(String inputPath) throws FileNotFoundException {
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            throw new FileNotFoundException("File directory does not exist.");
        }
        if (!inputFile.isFile()) {
            throw new FileNotFoundException("This directory does not represent a file.");
        }

    }
    /*
    This function does I/O operations using BufferedReader and BufferedWriter
     */
    public static void processInputOutput(String inputPath, String outputPath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputPath));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPath))) {

            String line;
            String outputString;

            // Reading and processing the input file line by line
            while ((line = bufferedReader.readLine()) != null) {
                // If necessary Utility.processLine returns the output string of the operation done
                outputString = Utility.processLine(line);

                if (outputString != null) {
                    bufferedWriter.write(outputString);
                    bufferedWriter.newLine();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
    Perceives which command is read and calls the desired function to get the operation done.
    Returns the output string for the line if needed.
     */
    private static String processLine(String line) {
        // Splits the read command into command and parameters
        String[] lineParts = line.split(" ");
        String commandType = lineParts[0];

        String user1, user2, userId, postId, content;
        String viewer, viewed;
        int num;
        switch (commandType) {
            case "create_user":
                user1 = lineParts[1];
                return Utility.createUser(user1);

            case "follow_user":
                user1 = lineParts[1];
                user2 = lineParts[2];
                return Utility.followUser(user1, user2);

            case "unfollow_user":
                user1 = lineParts[1];
                user2 = lineParts[2];
                return Utility.unfollowUser(user1, user2);

            case "create_post":
                userId = lineParts[1];
                postId = lineParts[2];
                content = lineParts[3];
                return Utility.createPost(userId, postId, content);

            case "see_post":
                userId = lineParts[1];
                postId = lineParts[2];
                return Utility.seePost(userId, postId);

            case "see_all_posts_from_user":
                viewer = lineParts[1];
                viewed = lineParts[2];
                return Utility.seeAllPostsFromUser(viewer, viewed);

            case "toggle_like":
                userId = lineParts[1];
                postId = lineParts[2];
                return Utility.toggleLike(userId, postId);

            case "generate_feed":
                userId = lineParts[1];
                num = Integer.parseInt(lineParts[2]);
                if (!Instagram.doesUserExist(userId)) {
                    return "Some error occurred in generate_feed.";
                }

                // Holds feed posts in an array list for further operations
                ArrayList<Post> feedArrayList = Utility.generateFeedArrayList(userId, num);
                StringBuilder feedBuilder = startBuildingFeed(userId, feedArrayList);

                // If there are not enough amount of posts to satisfy the number of posts wanted in the feed,
                // an extra line is added to the output the insufficient number of posts
                if (!checkIfEnough(feedArrayList, num)) {
                    feedBuilder.append("No more posts available for ").append(userId).append(".");
                } else {
                    // Deleting the extra new line
                    feedBuilder.setLength(feedBuilder.length() - 1);
                }

                return feedBuilder.toString();

            case "scroll_through_feed":
                userId = lineParts[1];
                num = Integer.parseInt(lineParts[2]);

                // Creating the array of which posts are liked (1) and which are not (0)
                int lengthOfLikes = lineParts.length - 3;
                int[] isLikedArray = new int[lengthOfLikes];

                for (int i = 0; i < lengthOfLikes; i++) {
                    isLikedArray[i] = Integer.parseInt(lineParts[i + 3]);
                }

                return Instagram.scrollThroughFeed(userId, num, isLikedArray);

            case "sort_posts":
                userId = lineParts[1];
                return Utility.sortPosts(userId);
        }

        // If the commands are given correctly this statement is never reached
        return null;
    }

    private static String sortPosts(String userId) {
        if (!Instagram.doesUserExist(userId)) {
            return "Some error occurred in sort_posts.";
        }

        MyMaxHeap<Post> sortedPosts = Instagram.getMaxHeapOfUserPosts(userId);

        if (sortedPosts.currentSize <= 0) {
            return "No posts from " + userId + ".";
        }

        StringBuilder sb = new StringBuilder("Sorting ").append(userId).append("'s posts:\n");

        Post nextPost;

        /* currentSize parameter is inherently decremented when an item is extracted from the heap,
        and we want to get every element in the heap; therefore, we can basically check if the heap size
        is greater than 0. The thing happening here is actually heap sort.
        */
        while (sortedPosts.currentSize > 0) {
            nextPost = sortedPosts.extractMax();
            sb.append(nextPost.id).append(", Likes: ").append(nextPost.numberOfLikes).append("\n");

            // If the element is the last one, delete the new line for the output string to be compatible with the rest
            // of the program
            if (sortedPosts.currentSize == 0) {
                sb.setLength(sb.length() - 1);
            }
        }

        return sb.toString();
    }

    private static StringBuilder startBuildingFeed(String userId, ArrayList<Post> feedArrayList) {
        StringBuilder sb = new StringBuilder("Feed for ").append(userId).append(":\n");

        // Traverse the feed array to create the output string
        for (Post post : feedArrayList) {
            sb.append("Post ID: ").append(post.id).append(", Author: ")
                    .append(post.author).append(", Likes: ").append(post.numberOfLikes).append("\n");
        }

        return sb;
    }

    private static boolean checkIfEnough(ArrayList<Post> feedArrayList, int num) {
        // If there are enough posts to satisfy the number of posts wanted in the feed
        return feedArrayList.size() == num;
    }

    protected static ArrayList<Post> generateFeedArrayList(String userId, int num) {
        MyMaxHeap<Post> postsToGenerateFeed = Instagram.getPostHeapForFeed(userId);

        ArrayList<Post> feedArrayList = new ArrayList<>();

        // Counter holds the number of posts added to the feed array
        int counter = 0;

        while (postsToGenerateFeed.currentSize > 0 && counter < num) {
            feedArrayList.add(postsToGenerateFeed.extractMax());
            counter++;
        }

        // Returns only the available posts, if the length of this array is smaller than wanted, then
        // this means that there was not enough posts to satisfy num
        return feedArrayList;
    }

    private static String toggleLike(String userId, String postId) {
        if (!Instagram.doesUserExist(userId) || !Instagram.doesPostExist(postId)) {
            return "Some error occurred in toggle_like.";
        }

        Post postToLike = Instagram.getPostObject(postId);

        // is true if the post is liked and false if the post is unliked
        boolean likedOrUnliked = postToLike.like(userId);

        User user = Instagram.getUserObject(userId);
        user.seenPosts.insert(new StringWrapper(postToLike.id));

        return userId + (likedOrUnliked ? " liked " : " unliked ") + postId + ".";
    }

    private static String seeAllPostsFromUser(String viewer, String viewed) {
        if (!Instagram.doesUserExist(viewer) || !Instagram.doesUserExist(viewed)) {
            return "Some error occurred in see_all_posts_from_user.";
        }

        // Add every post of the viewed to the seen of viewer
        for (MyLinkedList<StringWrapper> chainingList : Instagram.getUserObject(viewed).posts.hashArray) {
            if (chainingList != null) {
                for (StringWrapper particularPost : chainingList) {
                    seePost(viewer, particularPost.id);
                }
            }
        }

        return viewer + " saw all posts of " + viewed + ".";
    }

    private static String seePost(String userId, String postId) {
        if (!Instagram.doesUserExist(userId) || !Instagram.doesPostExist(postId)) {
            return "Some error occurred in see_post.";
        }

        User user = Instagram.getUserObject(userId);
        user.seenPosts.insert(new StringWrapper(postId));

        return userId + " saw " + postId + ".";
    }

    private static String createPost(String userId, String postId, String content) {
        if (!Instagram.doesUserExist(userId) || Instagram.doesPostExist(postId)) {
            return "Some error occurred in create_post.";
        }

        Post newPost = new Post(postId, 0, userId);
        Instagram.addPost(newPost);

        User user = Instagram.getUserObject(userId);
        StringWrapper postSW = new StringWrapper(postId);
        user.posts.insert(postSW);

        return userId + " created a post with Id " + postId + ".";
    }

    private static String followUser(String user1, String user2) {
        if (!Instagram.doesUserExist(user1) || !Instagram.doesUserExist(user2)) {
            return "Some error occurred in follow_user.";
        } else if (user1.equals(user2)) {
            return "Some error occurred in follow_user.";
        }

        User userObject1 = Instagram.getUserObject(user1);
        if (userObject1.following.contains(user2)) {
            return "Some error occurred in follow_user.";
        }

        userObject1.following.insert(new StringWrapper(user2));
        return user1 + " followed " + user2 + ".";
    }

    private static String unfollowUser(String user1, String user2) {
        if (!Instagram.doesUserExist(user1) || !Instagram.doesUserExist(user2)) {
            return "Some error occurred in unfollow_user.";
        } else if (user1.equals(user2)) {
            return "Some error occurred in unfollow_user.";
        }

        User userObject1 = Instagram.getUserObject(user1);
        if (!userObject1.following.contains(user2)) {
            return "Some error occurred in unfollow_user.";
        }

        userObject1.following.remove(user2);
        return user1 + " unfollowed " + user2 + ".";
    }

    private static String createUser(String userId) {
        if (!Instagram.doesUserExist(userId)) {
            User newUser = new User(userId);
            Instagram.addUser(newUser);
            return "Created user with Id " + userId + ".";
        } else {
            return "Some error occurred in create_user.";
        }
    }
}
