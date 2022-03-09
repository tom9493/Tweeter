package edu.byu.cs.tweeter.model.net.response;

public class UnfollowResponse extends Response {

    UnfollowResponse(String message) {
        super(false, message);
    }

    UnfollowResponse() {
        super(true);
    }

}
