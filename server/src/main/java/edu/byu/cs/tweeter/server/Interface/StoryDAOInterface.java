package edu.byu.cs.tweeter.server.Interface;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface StoryDAOInterface {
    public abstract List<Status> getStory(User user, int pageSize, Status lastStatus);
    public abstract void addStatusToStory(Status status) throws Exception;
}
