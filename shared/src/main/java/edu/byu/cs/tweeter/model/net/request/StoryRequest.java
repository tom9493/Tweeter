package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class StoryRequest extends PagedStatusRequest {

    private StoryRequest() { super(null, null, 0, null); }

    public StoryRequest(AuthToken authToken, String userAlias, int limit, Status lastStatus) {
        super(authToken, userAlias, limit, lastStatus);
    }
}