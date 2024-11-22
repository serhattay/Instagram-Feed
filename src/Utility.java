import java.io.*;
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
                return Utility.generateFeed(userId, num);
        }

        return null;
    }

    private static String generateFeed(String userId, int num) {
        if (!Instagram.doesUserExist(userId)) {
            return "Some error occurred in generate_feed.";
        }

        StringBuilder feed = new StringBuilder("Feed for ").append(userId).append("\n");


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
