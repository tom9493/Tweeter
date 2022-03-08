package edu.byu.cs.tweeter.model.net.response;

public class FeedResponse extends PagedResponse {

    FeedResponse(boolean success, boolean hasMorePages) {
        super(success, hasMorePages);
    }

    FeedResponse(boolean success, String message, boolean hasMorePages) {
        super(success, message, hasMorePages);
    }
}
