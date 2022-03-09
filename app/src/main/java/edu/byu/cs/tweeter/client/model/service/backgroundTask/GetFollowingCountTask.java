package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

import java.io.IOException;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    private static final String LOG_TAG = "GetFollowingCountTask";
    static final String URL_PATH = "/getfollowingcount";

    private FollowingCountRequest request;

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
        this.request = new FollowingCountRequest(authToken, targetUser.getAlias());
    }

    @Override
    protected int runCountTask() {
        try {
            FollowingCountResponse response = getServerFacade().getFollowingCount(request, URL_PATH);

            if(response.isSuccess()) {
                return response.getCount();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get following count", ex);
            sendExceptionMessage(ex);
        }
        return -1;

    }
}
