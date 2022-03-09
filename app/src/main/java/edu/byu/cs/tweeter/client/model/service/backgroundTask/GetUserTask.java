package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

import java.io.IOException;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {

    public static final String USER_KEY = "user";
    private static final String LOG_TAG = "GetUserTask";
    static final String URL_PATH = "/getuser";

    private UserRequest request;
    private User user;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(authToken, messageHandler);
        this.request = new UserRequest(authToken, alias);
    }

    @Override
    protected void runTask() {
        user = getUser();

        if (user != null) {
            sendSuccessMessage();
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }

    private User getUser() {
        try {
            UserResponse response = getServerFacade().getUser(request, URL_PATH);

            if(response.isSuccess()) {
                return response.getUser();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get User", ex);
            sendExceptionMessage(ex);
        }
        return null;
    }
}
