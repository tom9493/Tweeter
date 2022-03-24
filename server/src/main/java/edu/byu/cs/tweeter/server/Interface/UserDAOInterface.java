package edu.byu.cs.tweeter.server.Interface;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAOInterface {
    public abstract User addUser(String username, String password, String firstName, String lastName, String imageURL);
    public abstract User getUser(String userAlias);
    public abstract User getUser(String username, String password);
}
