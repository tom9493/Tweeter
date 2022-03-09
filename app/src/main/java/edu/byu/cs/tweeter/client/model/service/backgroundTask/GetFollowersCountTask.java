package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

import java.io.IOException;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowersCountTask";
    static final String URL_PATH = "/getfollowerscount";

    private FollowersCountRequest request;

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
        this.request = new FollowersCountRequest(authToken, targetUser.getAlias());
    }

    @Override
    protected int runCountTask() {
        try {
            FollowersCountResponse response = getServerFacade().getFollowersCount(request, URL_PATH);

            if(response.isSuccess()) {
                return response.getCount();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers count", ex);
            sendExceptionMessage(ex);
        }
        return -1;
    }
}
