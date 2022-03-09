package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

import java.io.IOException;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {

    public static final String IS_FOLLOWER_KEY = "is-follower";
    private static final String LOG_TAG = "IsFollowerTask";
    static final String URL_PATH = "/isfollower";

    private IsFollowerRequest request;

    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.request = new IsFollowerRequest(authToken, follower, followee);
    }

    @Override
    protected void runTask() {
        try {
            IsFollowerResponse response = getServerFacade().isFollower(request, URL_PATH);
            if(response.isSuccess()) {
                this.isFollower = response.isFollower();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed isFollower task", ex);
            sendExceptionMessage(ex);
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}
