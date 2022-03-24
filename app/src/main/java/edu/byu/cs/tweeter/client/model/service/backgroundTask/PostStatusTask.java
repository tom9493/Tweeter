package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

import java.io.IOException;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {

    private static final String LOG_TAG = "PostStatusTask";
    static final String URL_PATH = "/poststatus";

    private PostStatusRequest request;

    public PostStatusTask(AuthToken authToken, Status status, long timeStamp, Handler messageHandler) {
        super(authToken, messageHandler);
        this.request = new PostStatusRequest(authToken, status, timeStamp);
    }

    @Override
    protected void runTask() {
        try {
            PostStatusResponse response = getServerFacade().postStatus(request, URL_PATH);

            if(response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to post status", ex);
            sendExceptionMessage(ex);
        }
    }

}
