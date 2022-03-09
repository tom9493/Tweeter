package edu.byu.cs.tweeter.server.dao;

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
import edu.byu.cs.tweeter.util.FakeData;

import java.util.List;
import java.util.Objects;

public class UserDAO {

    public LoginResponse login(LoginRequest request) {
        return new LoginResponse(getFakeData().getFirstUser(), getDummyAuthToken());
    }

    public RegisterResponse register(RegisterRequest request) {
        return new RegisterResponse(getFakeData().getFirstUser(), getDummyAuthToken());
    }

    public UserResponse getUser(UserRequest request) {
        List<User> users = getFakeUsers();
        for (int i = 0; i < users.size(); ++i) {
            if (Objects.equals(users.get(i).getAlias(), request.getUserAlias())) {
                return new UserResponse(users.get(i));
            }
        }
        return new UserResponse("Not a valid user");
    }

    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse();
    }

    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    List<User> getFakeUsers() { return getFakeData().getFakeUsers(); }

    FakeData getFakeData() {
        return new FakeData();
    }


}
