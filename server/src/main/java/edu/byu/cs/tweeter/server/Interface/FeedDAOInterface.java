package edu.byu.cs.tweeter.server.Interface;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface FeedDAOInterface {
    public abstract List<Status> getFeed(User user, int pageSize, Status lastStatus);
    public abstract void addStatusToFeed(String userAlas, Status status) throws Exception;
}
