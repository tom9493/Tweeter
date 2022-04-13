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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class UserService extends ValidationService {
    public UserService(AbstractFactory factory) { super(factory); }

    public LoginResponse login(LoginRequest request) {
        try {
            validateLogin(request.getUsername(), request.getPassword());

            User user = getUserDAO().getUser(request.getUsername(), hash(request.getPassword()));
            if (user == null) { return new LoginResponse("Incorrect login information. Try again..."); }
            AuthToken authToken = makeAuthToken();
            getAuthTokenDAO().insertAuthToken(authToken);
            return new LoginResponse(user, authToken);
        } catch (Exception ex) {
            return new LoginResponse(ex.getMessage() + "Issue with login");
        }
    }

    public RegisterResponse register(RegisterRequest request) {
        try {
            validateLogin(request.getUsername(), request.getPassword());

            // addUser returns null if the user already exists, and is a register failure
            User user = getUserDAO().addUser(request.getUsername(), hash(request.getPassword()), request.getFirstName(),
                    request.getLastName(), getImageDAO().getImageURL(request.getUsername() + "_photo.png"));
            if (user == null) { return new RegisterResponse("User with that username already exists..."); }

            byte[] byteArray = Base64.getDecoder().decode(request.getImage());
            InputStream stream = new ByteArrayInputStream(byteArray);
            getImageDAO().uploadImage(request.getUsername(), stream);

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
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String token = new String(array, StandardCharsets.UTF_8);
        return new AuthToken(token, System.currentTimeMillis());
    }

    public String hash(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : resultByteArray) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    UserDAOInterface getUserDAO() { return factory.getUserDAO(); }
    ImageDAOInterface getImageDAO() { return factory.getImageDAO(); }
}
