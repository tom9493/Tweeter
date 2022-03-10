package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersRequest extends PagedUserRequest {

    private FollowersRequest() { super(null, null, 0, null); };

    public FollowersRequest(AuthToken authToken, User user, int limit, User lastFollower) {
        super(authToken, user, limit, lastFollower);
    }
}
