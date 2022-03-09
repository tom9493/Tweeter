package edu.byu.cs.tweeter.model.net.response;

public class PostStatusResponse extends Response {

    PostStatusResponse(boolean success, String message) {
        super(success, message);
    }

    PostStatusResponse(boolean success) {
        super(success);
    }
}