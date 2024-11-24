
# Instagram Feed

## 1. Introduction

You found a mysterious device in your basement and it took you back in time to early 2010. After learning about your time travel, Kevin Systrom, developer of Instagram, hired you to create a **Feed Manager** for his new idea, Instagram. You are expected to develop a Java program which keeps track of users, their posts and likes, users’ feeds, and creates a log about these operations.

## 2. Application’s Capabilities

Kevin wants the application to be able to:

- **Create users and posts**
- **Enable users to interact with posts and other users**
- **Suggest new posts based on the interactions that posts receive**

### 2.1 How Should It Work

- **User Management:**
  - The application should be able to create users with an ID.
  - A user should be able to follow another user or unfollow an already followed user.

- **Post Management:**
  - All users should be able to create posts with their ID and content inside it.
  - A user should easily be able to see another user’s all posts and like or unlike them.
  - Generate a feed for a user according to post likes and the user’s seen posts.
  - Sort a user’s posts with respect to its likes.

## 3. I/O Files

- **Input:** A file of instructions that should be executed.
- **Output:** A log that records the events that happen during execution.

## 4. Functionalities

### 4.1 User Creation

A new user should be created with its `userId`.

- **Input Format:**

  ```plaintext
  create_user userId
  ```

### 4.2 Following/Unfollowing a User

A user with the ID `<userId1>` should be able to follow or unfollow another user with the ID `<userId2>`.

- **Input Format:**

  ```plaintext
  follow_user <userId1> <userId2>
  ```

  or

  ```plaintext
  unfollow_user <userId1> <userId2>
  ```

### 4.3 Creating a Post

A user with the ID `<userId>` should be able to create a post with `<postId>` and `<content>`.

- **Input Format:**

  ```plaintext
  create_post <userId> <postId> <content>
  ```

### 4.4 Seeing a Post

A user with `<userId>` sees the post with ID `<postId>`.

- **Input Format:**

  ```plaintext
  see_post <userId> <postId>
  ```

- **Example:**

  ```plaintext
  see_post user1 post1
  ```

### 4.5 Seeing All Posts of A User

A user with ID `<viewerId>` can see a user with ID `<viewedId>`’s all posts.

- **Input Format:**

  ```plaintext
  see_all_posts_from_user <viewerId> <viewedId>
  ```

### 4.6 Pressing the Like Button

When the like button is pressed, if the post with `<postId>` is not already liked by the user with ID `<userId>`, it should be liked; otherwise, it shouldn’t be liked. Liking a post also counts as seeing a post.

- **Input Format:**

  ```plaintext
  toggle_like <userId> <postId>
  ```

### 4.7 Generating Feed

Generate a `<num>` post long feed for a user with ID `<userId>`. The posts that the user created and the posts that the user already seen shouldn’t show up. The most liked post appears first.

- **Input Format:**

  ```plaintext
  generate_feed <userId> <num>
  ```

### 4.8 Scrolling Through Feed

When this instruction is given, the user with ID `<userId>` should scroll through `<num>` number of posts in its feed. Note that `num` is always a positive integer. Next to the `<num>`, there are `<num>` number of `1`s or `0`s. If you encounter a `1`, it means the user clicked the like button for that post; `0` means they did not.

- **Input Format:**

  ```plaintext
  scroll_through_feed <userId> <num> <like1> <like2> ... <likenum>
  ```

### 4.9 Sorting Posts

Sort a user’s all posts and log all the posts according to their likes from bigger to smaller. If two posts have equal likes, the lexicographically bigger one comes first.

- **Input Format:**

  ```plaintext
  sort_posts <userId>
  ```

## 5. Usage

### 5.1 Running the Program

1. **Compile the Java Program:**
   
   ```bash
   javac InstagramFeedManager.java
   ```

2. **Run the Program with Input File:**
   
   ```bash
   java InstagramFeedManager input.txt output.txt
   ```

   - Replace `input.txt` with the path to your input file containing the instructions.
   - Replace `output.txt` with the path where you want the output.txt file to be.

### 5.2 Input File Structure

- The input file should contain a series of instructions as described in the **Functionalities** section.
- Each instruction should be on a separate line following the specified format.

### 5.3 Output Log

- The program will generate a log file recording all events that happen during execution.
- Ensure that the program has the necessary permissions to create and write to the log file.

## 6. Example

### 6.1 Sample Input

```plaintext
toggle_like eoiYjoGr Dbq5tw3b
create_post BDYkQ3BZ post00000001 aIRBLdEW
create_user user00000001
create_post user00000001 post00000002 rG3x9ya9
create_user user00000002
create_post user00000002 post00000003 DgYmKi4S
create_post user00000002 post00000004 32Km1VMd
create_post user00000002 post00000005 eeifg3UL
create_post user00000001 post00000006 V2mUy4mW
create_user user00000003
```

### 6.2 Expected Log Output

```plaintext
Some error occurred in toggle_like.
Some error occurred in create_post.
Created user with Id user00000001.
user00000001 created a post with Id post00000002.
Created user with Id user00000002.
user00000002 created a post with Id post00000003.
user00000002 created a post with Id post00000004.
user00000002 created a post with Id post00000005.
user00000001 created a post with Id post00000006.
Created user with Id user00000003.
```

*Happy Coding!*
