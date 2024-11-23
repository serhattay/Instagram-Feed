import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Utility {
    public static void checkDirectories(String inputPath, String outputPath) throws FileNotFoundException {
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            throw new FileNotFoundException("File directory does not exist.");
        }
        if (!inputFile.isFile()) {
            throw new FileNotFoundException("This directory does not represent a file.");
        }

        File outputFile = new File(outputPath);
        File outputDirectory = outputFile.getParentFile();

        if (!outputDirectory.exists()) {
            throw new FileNotFoundException("Output directory is not valid.");
        }

    }
    public static void processInputOutput(String inputPath, String outputPath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputPath));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputPath))) {

            String line;
            String outputString;

            while ((line = bufferedReader.readLine()) != null) {
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

    private static String processLine(String line) {
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

                ArrayList<Post> feedArrayList = Utility.generateFeedArrayList(userId, num);
                StringBuilder feedBuilder = startBuildingFeed(userId, feedArrayList);
                if (!checkIfEnough(feedArrayList, num)) {
                    feedBuilder.append("No more posts available for ").append(userId).append(".");
                } else {
                    feedBuilder.setLength(feedBuilder.length() - 1);
                }

                return feedBuilder.toString();

            case "scroll_through_feed":
                userId = lineParts[1];
                num = Integer.parseInt(lineParts[2]);
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

        return null;
    }

    private static String sortPosts(String userId) {
        if (!Instagram.doesUserExist(userId)) {
            return "Some error occurred in sort_posts.";
        }

        MyMaxHeap<Post> sortedPosts = Instagram.getMaxHeapOfUserPosts(userId);

        if (sortedPosts.currentSize <= 0) {
            return "No posts from " + userId;
        }

        StringBuilder sb = new StringBuilder("Sorting ").append(userId).append("'s posts:\n");

        Post nextPost;
        while (sortedPosts.currentSize > 0) {
            nextPost = sortedPosts.extractMax();
            sb.append(nextPost.id).append(", Likes: ").append(nextPost.numberOfLikes).append("\n");

            if (sortedPosts.currentSize == 0) {
                sb.setLength(sb.length() - 1);
            }
        }

        return sb.toString();
    }

    private static StringBuilder startBuildingFeed(String userId, ArrayList<Post> feedArrayList) {
        StringBuilder sb = new StringBuilder("Feed for ").append(userId).append(":\n");

        for (Post post : feedArrayList) {
            sb.append("Post ID: ").append(post.id).append(", Author: ")
                    .append(post.author).append(", Likes: ").append(post.numberOfLikes).append("\n");
        }

        return sb;
    }

    private static boolean checkIfEnough(ArrayList<Post> feedArrayList, int num) {
        return feedArrayList.size() == num;
    }

    protected static ArrayList<Post> generateFeedArrayList(String userId, int num) {
        MyMaxHeap<Post> postsToGenerateFeed = Instagram.getPostHeapForFeed(userId);
        int counter = 0;
        ArrayList<Post> feedArrayList = new ArrayList<>();
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
