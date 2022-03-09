package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;
import java.util.Objects;

public class FeedResponse extends PagedResponse {

    private List<Status> feed;

    FeedResponse(String message) {
        super(message, false);
    }

    FeedResponse(List<Status> feed, boolean hasMorePages) {
        super(hasMorePages);
        this.feed = feed;
    }

    public List<Status> getFeed() {
        return feed;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FeedResponse that = (FeedResponse) param;

        return (Objects.equals(feed, that.feed) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(feed);
    }
}
