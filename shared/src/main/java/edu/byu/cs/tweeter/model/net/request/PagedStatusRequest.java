package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedStatusRequest extends PagedRequest {

    private Status lastStatus;
    public PagedStatusRequest(AuthToken authToken, User user, int limit, Status lastStatus) {
        super(authToken, user, limit);
        this.lastStatus = lastStatus;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}
