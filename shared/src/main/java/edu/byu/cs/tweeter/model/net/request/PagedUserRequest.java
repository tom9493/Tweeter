package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedUserRequest extends PagedRequest {

    private User lastUser;

    public PagedUserRequest(AuthToken authToken, User user, int limit, User lastUser) {
        super(authToken, user, limit);
        this.lastUser = lastUser;
    }

    public User getLastUser() {
        return lastUser;
    }

    public void setLastUser(User lastUser) {
        this.lastUser = lastUser;
    }
}
