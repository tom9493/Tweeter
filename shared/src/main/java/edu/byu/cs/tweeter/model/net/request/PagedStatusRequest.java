package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public abstract class PagedStatusRequest extends PagedRequest {

    private Status lastStatus;
    public PagedStatusRequest(AuthToken authToken, String userAlias, int limit, Status lastStatus) {
        super(authToken, userAlias, limit);
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}
