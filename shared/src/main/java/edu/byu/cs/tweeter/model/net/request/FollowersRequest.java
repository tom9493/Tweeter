package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowersRequest extends PagedUserRequest {

    private FollowersRequest() { super(null, null, 0, null); };

    public FollowersRequest(AuthToken authToken, String userAlias, int limit, String lastFollowerAlias) {
        super(authToken, userAlias, limit, lastFollowerAlias);
    }
}
