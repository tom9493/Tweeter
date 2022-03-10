package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FollowingRequest extends PagedUserRequest {

    private FollowingRequest() {super(null,null,0,null); }

    public FollowingRequest(AuthToken authToken, User follower, int limit, User lastFollowee) {
        super(authToken, follower, limit, lastFollowee);
    }
}
