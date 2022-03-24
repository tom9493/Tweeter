package edu.byu.cs.tweeter.server.Interface;

public interface AbstractFactory {
    public abstract AuthTokenDAOInterface getAuthTokenDAO();
    public abstract FeedDAOInterface getFeedDAO();
    public abstract FollowsDAOInterface getFollowsDAO();
    public abstract StoryDAOInterface getStoryDAO();
    public abstract UserDAOInterface getUserDAO();
    public abstract ImageDAOInterface getImageDAO();
}
