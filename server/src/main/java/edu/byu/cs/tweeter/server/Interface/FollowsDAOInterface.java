package edu.byu.cs.tweeter.server.Interface;

import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface FollowsDAOInterface {
    public abstract boolean isFollower(String followerAlias, String followeeAlias);
    public abstract List<String> getFollowers(String userAlias);
    public abstract List<String> getFollowing(String userAlias);
    public abstract int getFollowersCount(String userAlias);
    public abstract int getFollowingCount(String userAlias);
    public abstract void follow(String followerAlias, String followeeAlias);
    public abstract void unfollow(String followerAlias, String followeeAlias);
}
