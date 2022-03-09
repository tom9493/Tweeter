package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {

    private boolean isFollower;

    IsFollowerResponse(String message) {
        super(false, message);
    }

    IsFollowerResponse(boolean isFollower) {
        super(true);
        this.isFollower = isFollower;
    }

    public boolean isFollower() {
        return isFollower;
    }
}