package model;

public class Subscription {

    // ID of most recent transaction
    String id;

    public Subscription(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
