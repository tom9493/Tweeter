package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedRequest extends PagedStatusRequest {

    private FeedRequest() { super(null, null, 0, null); }

    public FeedRequest(AuthToken authToken, String userAlias, int limit, Status lastStatus) {
        super(authToken, userAlias, limit, lastStatus);
    }
}
