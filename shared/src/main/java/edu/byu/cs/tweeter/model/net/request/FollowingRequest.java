package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followees for a specified follower.
 */
public class FollowingRequest extends PagedUserRequest {

    private FollowingRequest() {super(null,null,0,null); }

    public FollowingRequest(AuthToken authToken, String followerAlias, int limit, String lastFolloweeAlias) {
        super(authToken, followerAlias, limit, lastFolloweeAlias);
    }
}
