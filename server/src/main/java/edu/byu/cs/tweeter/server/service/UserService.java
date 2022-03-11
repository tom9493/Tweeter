package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[BadRequest] Login Request missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Login Request missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.

        return getUserDAO().login(request);
    }

    public RegisterResponse register(RegisterRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[BadRequest] Register Request missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Register Request missing a password");
        }

        return getUserDAO().register(request);
    }

    public UserResponse getUser(UserRequest request) {
        if (!request.getAuthToken().equals(getDummyAuthToken())) {
            throw new RuntimeException("[BadRequest] User Request needs valid authToken");
        } else if (request.getUserAlias() == null) {
            throw new RuntimeException("[BadRequest] User Request needs valid user alias");
        }
        return getUserDAO().getUser(request);
    }

    public LogoutResponse logout(LogoutRequest request) {
        return getUserDAO().logout(request);
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }

    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
