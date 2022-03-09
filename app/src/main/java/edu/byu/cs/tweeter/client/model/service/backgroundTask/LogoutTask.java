package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

import java.io.IOException;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    private static final String LOG_TAG = "LogoutTask";
    static final String URL_PATH = "/logout";

    private LogoutRequest request;

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
        this.request = new LogoutRequest(authToken);
    }

    @Override
    protected void runTask() {
        try {
            LogoutResponse response = getServerFacade().logout(request, URL_PATH);

            if(response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to logout", ex);
            sendExceptionMessage(ex);
        }
    }
}
