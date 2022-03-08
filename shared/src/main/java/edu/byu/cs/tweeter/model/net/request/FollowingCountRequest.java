package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowingCountRequest extends GetCountRequest {

    private FollowingCountRequest() { super(null, null); }

    public FollowingCountRequest(AuthToken authToken, String userAlias) {
        super(authToken, userAlias);
    }
}
