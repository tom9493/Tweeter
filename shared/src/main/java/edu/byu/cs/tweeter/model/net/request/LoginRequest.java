package edu.byu.cs.tweeter.model.net.request;

/**
 * Contains all the information needed to make a login request.
 */
public class LoginRequest extends LogRegRequest {

    private LoginRequest() { super(null, null); }

    public LoginRequest(String username, String password) {
        super(username, password);
    }

}
