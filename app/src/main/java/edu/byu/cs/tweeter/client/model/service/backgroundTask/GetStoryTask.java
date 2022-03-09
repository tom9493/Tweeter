package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String LOG_TAG = "GetStoryTask";
    static final String URL_PATH = "/getstory";

    private StoryRequest request;

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
        this.request = new StoryRequest(authToken, targetUser.getAlias(), limit, lastStatus);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        try {
            StoryResponse response = getServerFacade().getStory(request, URL_PATH);

            if(response.isSuccess()) {
                return new Pair<>(response.getStory(), response.getHasMorePages());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get story", ex);
            sendExceptionMessage(ex);
        }
        return null;
        //return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }
}
