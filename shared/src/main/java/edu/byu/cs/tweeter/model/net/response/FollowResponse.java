package edu.byu.cs.tweeter.model.net.response;

public class FollowResponse extends Response {

    FollowResponse(String message) {
        super(false, message);
    }

    FollowResponse() {
        super(true);
    }
}