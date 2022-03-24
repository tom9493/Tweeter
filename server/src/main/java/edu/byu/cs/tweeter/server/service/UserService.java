package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.Interface.AbstractFactory;
import edu.byu.cs.tweeter.server.Interface.ImageDAOInterface;
import edu.byu.cs.tweeter.server.Interface.UserDAOInterface;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

public class UserService extends ValidationService {
    public UserService(AbstractFactory factory) { super(factory); }

    public LoginResponse login(LoginRequest request) {
        try {
            validateLogin(request.getUsername(), request.getPassword());

            User user = getUserDAO().getUser(request.getUsername(), request.getPassword());
            if (user == null) { return new LoginResponse("Incorrect login information. Try again..."); }
            AuthToken authToken = makeAuthToken();
            getAuthTokenDAO().insertAuthToken(authToken);
            return new LoginResponse(user, authToken);
        } catch (Exception ex) {
            return new LoginResponse(ex.getMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request) {
        try {
            validateLogin(request.getUsername(), request.getPassword());

            // addUser returns null if the user already exists, and is a register failure
            User user = getUserDAO().addUser(request.getUsername(), request.getPassword(), request.getFirstName(),
                    request.getLastName(), getImageDAO().getImageURL(request.getUsername() + "_photo.png"));
            if (user == null) { return new RegisterResponse("User with that username already exists..."); }

            byte[] byteArray = Base64.getDecoder().decode(request.getImage());
            InputStream stream = new ByteArrayInputStream(byteArray);
            getImageDAO().uploadImage(request.getUsername(), stream);

            //user.setImageUrl(user.getImageUrl().replace("https","http"));

            AuthToken authToken = makeAuthToken();
            getAuthTokenDAO().insertAuthToken(authToken);
            return new RegisterResponse(user, authToken);
        } catch (Exception ex) {
            return new RegisterResponse(ex.getMessage());
        }
    }

    public UserResponse getUser(UserRequest request) {
        try {
            validateAuthToken(request.getAuthToken());
            User user = getUserDAO().getUser(request.getUserAlias());
            if (user == null) { return new UserResponse("user_alias provided not in user table..."); }
            return new UserResponse(user);
        } catch (Exception ex) {
            return new UserResponse(ex.getMessage());
        }
    }

    public LogoutResponse logout(LogoutRequest request) {
        getAuthTokenDAO().deleteAuthToken(request.getAuthToken());
        return new LogoutResponse();
    }

    public void validateLogin(String username, String password) {
        if (username == null){
            throw new RuntimeException("[BadRequest] Login Request missing a username");
        } else if(password == null) {
            throw new RuntimeException("[BadRequest] Login Request missing a password");
        }
    }

    // MAKE THIS HASH THE PASSWORD
    public AuthToken makeAuthToken() {
        String token = "DummyAuthToken123";
        return new AuthToken(token, System.currentTimeMillis());
    }

    UserDAOInterface getUserDAO() { return factory.getUserDAO(); }
    ImageDAOInterface getImageDAO() { return factory.getImageDAO(); }
}
