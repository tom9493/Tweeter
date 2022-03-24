package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.Interface.AbstractFactory;
import edu.byu.cs.tweeter.server.Interface.UserDAOInterface;

public abstract class PagedService extends ValidationService {
    public PagedService(AbstractFactory factory) { super(factory); }

    protected void validatePagedRequest(String userAlias, int limit, AuthToken authToken) throws Exception {
        validateAuthToken(authToken);
        if(getUserDAO().getUser(userAlias) == null) {
            throw new RuntimeException("[BadRequest] Following Request needs to have a valid follower alias");
        } else if(limit <= 0) {
            throw new RuntimeException("[BadRequest] Following Request needs to have a positive limit");
        }
    }

    protected UserDAOInterface getUserDAO() { return factory.getUserDAO(); }
}
