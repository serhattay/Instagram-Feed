import java.io.*;

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

        String user1;
        String user2;
        String userId;
        String postId;
        String content;
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
        }

        return null;
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
