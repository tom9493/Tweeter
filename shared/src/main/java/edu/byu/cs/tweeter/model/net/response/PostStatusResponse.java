package edu.byu.cs.tweeter.model.net.response;

public class PostStatusResponse extends Response {

    PostStatusResponse(String message) {
        super(false, message);
    }

    PostStatusResponse() {
        super(true);
    }
}