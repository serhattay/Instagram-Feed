public class Post implements Denominable {
    String id;
    int numberOfLikes;

    Post() {
        id = null;
        numberOfLikes = 0;
    }

    Post(String id) {
        this(id, 0);
    }
    Post(String id, int numberOfLikes) {
        this.id = id;
        this.numberOfLikes = numberOfLikes;
    }


    @Override
    public String getId() {
        return id;
    }
}
