package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    public static final String FOLLOWERS_KEY = "followers";

    private static final String LOG_TAG = "GetFollowersTask";
    static final String URL_PATH = "/getfollowers";

    private FollowersRequest request;

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
        this.request = new FollowersRequest(authToken, targetUser.getAlias(), limit, lastFollower.getAlias());
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        try {
            FollowersResponse response = getServerFacade().getFollowers(request, URL_PATH);

            if(response.isSuccess()) {
                return new Pair<>(response.getFollowers(), response.getHasMorePages());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers", ex);
            sendExceptionMessage(ex);
        }
        return null;
    }
}
