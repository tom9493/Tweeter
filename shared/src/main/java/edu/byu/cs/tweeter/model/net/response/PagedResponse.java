package edu.byu.cs.tweeter.model.net.response;

/**
 * A response that can indicate whether there is more data available from the server.
 */
public class PagedResponse extends Response {

    private final boolean hasMorePages;

    PagedResponse(String message, boolean hasMorePages) {
        super(false, message);
        this.hasMorePages = hasMorePages;
    }

    PagedResponse(boolean hasMorePages) {
        super(true);
        this.hasMorePages = hasMorePages;
    }

    /**
     * An indicator of whether more data is available from the server. A value of true indicates
     * that the result was limited by a maximum value in the request and an additional request
     * would return additional data.
     *
     * @return true if more data is available; otherwise, false.
     */
    public boolean getHasMorePages() {
        return hasMorePages;
    }
}
