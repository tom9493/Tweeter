package edu.byu.cs.tweeter.model.net.response;

public class LogoutResponse extends Response {

    LogoutResponse(boolean success, String message) {
        super(success, message);
    }

    LogoutResponse(boolean success) {
        super(success);
    }
}