package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Following Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Following Request needs to have a positive limit");
        }
        return getFollowDAO().getFollowees(request);
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if(request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Followers Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Followers Request needs to have a positive limit");
        }
        return getFollowDAO().getFollowers(request);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFollower().getAlias() == null) {
            throw new RuntimeException("[BadRequest] IsFollower Request needs specified follower");
        } else if (request.getFollowee().getAlias() == null) {
            throw new RuntimeException("[BadRequest] IsFollower Request needs specified followee");
        }
        return getFollowDAO().isFollower(request);
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getFollowee().getAlias() == null) {
            throw new RuntimeException("[BadRequest] Follow Request needs specified followee");
        }
        return getFollowDAO().follow(request);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getFollowee().getAlias() == null) {
            throw new RuntimeException("[BadRequest] Unfollow Request needs specified followee");
        }
        return getFollowDAO().unfollow(request);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[BadRequest] Followers Count Request needs specified user alias");
        }
        return getFollowDAO().getFollowerCount(request);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[BadRequest] Following Count Request needs specified user alias");
        }
        return getFollowDAO().getFollowingCount(request);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
