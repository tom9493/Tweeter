package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest extends PagedStatusRequest {

    private StoryRequest() { super(null, null, 0, null); }

    public StoryRequest(AuthToken authToken, User user, int limit, Status lastStatus) {
        super(authToken, user, limit, lastStatus);
    }
}