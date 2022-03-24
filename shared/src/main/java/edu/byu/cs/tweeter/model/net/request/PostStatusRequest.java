package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest {
    private AuthToken authToken;
    private Status status;
    private long timeStamp;

    private PostStatusRequest() {}

    public PostStatusRequest(AuthToken authToken, Status status, long timeStamp) {
        this.authToken = authToken;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getTimeStamp() { return timeStamp; }

    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }
}
