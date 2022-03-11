package edu.byu.cs.tweeter.client.model.net;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class ServerFacadeIntTest {
    private ServerFacade serverFacade;
    private AuthToken authToken;
    private User mockUser;
    private int limit;

    @Before
    public void setup() {
        serverFacade = new ServerFacade();
        authToken = new AuthToken("token", "datetime"); // Matches required mock authtoken in server
    }

    @Test
    public void testRegister() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest("firstName", "lastName", "username",
                "password", null);
        RegisterResponse response = serverFacade.register(request, "/register");

        Assert.assertTrue(response.isSuccess());
        Assert.assertNull(response.getMessage());
        Assert.assertEquals(response.getAuthToken(), authToken);
    }

    @Test
    public void testGetFollowers() throws IOException, TweeterRemoteException {
        mockUser = Mockito.mock(User.class);
        limit = 10;

        FollowersRequest request = new FollowersRequest(authToken, mockUser, limit, mockUser);
        FollowersResponse response = serverFacade.getFollowers(request, "/getfollowers");

        Assert.assertTrue(response.isSuccess());
        Assert.assertNull(response.getMessage());
        Assert.assertTrue(response.getHasMorePages());
        Assert.assertEquals(response.getFollowers().size(), limit);
    }

    @Test
    public void testGetFollowersCount() throws IOException, TweeterRemoteException {
        FollowersCountRequest request = new FollowersCountRequest(authToken, "alias");
        FollowersCountResponse response = serverFacade.getFollowersCount(request, "getfollowerscount");

        Assert.assertTrue(response.isSuccess());
        Assert.assertNull(response.getMessage());
        Assert.assertEquals(response.getCount(), 21); // Number of dummy followers
    }
}
