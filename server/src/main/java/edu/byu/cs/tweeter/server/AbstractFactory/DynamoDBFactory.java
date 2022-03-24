package edu.byu.cs.tweeter.server.AbstractFactory;
import edu.byu.cs.tweeter.server.Interface.*;
import edu.byu.cs.tweeter.server.dao.*;

public class DynamoDBFactory implements AbstractFactory {
    @Override
    public AuthTokenDAOInterface getAuthTokenDAO() { return new AuthTokenDAO(); }

    @Override
    public FeedDAOInterface getFeedDAO() { return new FeedDAO(); }

    @Override
    public FollowsDAOInterface getFollowsDAO() { return new FollowsDAO(); }

    @Override
    public StoryDAOInterface getStoryDAO() { return new StoryDAO(); }

    @Override
    public UserDAOInterface getUserDAO() { return new UserDAO(); }

    @Override
    public ImageDAOInterface getImageDAO() { return new ImageDAO(); }
}
