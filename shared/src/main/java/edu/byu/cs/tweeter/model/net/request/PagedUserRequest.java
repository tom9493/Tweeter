package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class PagedUserRequest extends PagedRequest {

    private String lastUserAlias;

    public PagedUserRequest(AuthToken authToken, String userAlias, int limit, String lastUserAlias) {
        super(authToken, userAlias, limit);
        this.lastUserAlias = lastUserAlias;
    }

    public String getLastUserAlias() {
        return lastUserAlias;
    }

    public void setLastUserAlias(String lastUserAlias) {
        this.lastUserAlias = lastUserAlias;
    }
}
