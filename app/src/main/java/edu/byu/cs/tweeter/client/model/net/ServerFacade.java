package edu.byu.cs.tweeter.client.model.net;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;

import java.io.IOException;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {
    private static final String SERVER_URL = "https://f1gl401d40.execute-api.us-west-1.amazonaws.com/TweeterStage";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);
    }

    public LogoutResponse logout(LogoutRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);
    }

    public FollowingResponse getFollowees(FollowingRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
    }

    public FollowersResponse getFollowers(FollowersRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowersResponse.class);
    }

    public FeedResponse getFeed(FeedRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FeedResponse.class);
    }

    public StoryResponse getStory(StoryRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, StoryResponse.class);
    }

    public FollowResponse follow(FollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowResponse.class);
    }

    public UnfollowResponse unfollow(UnfollowRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, UnfollowResponse.class);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, IsFollowerResponse.class);
    }

    public UserResponse getUser(UserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, UserResponse.class);
    }

    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowersCountResponse.class);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowingCountResponse.class);
    }
}