package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

// NEXT, THE VIEW INTERFACES HAVE A LOT IN COMMON, SO MAKE ONE THEY ALL INHERIT FROM, AS WELL AS
// MORE SPECIFIC ONES LIKE WE DID FOR THE HANDLERS.

// ALSO MAKE ANOTHER PRESENTER CLASS FOR THE FEED, STORY, FOLLOWERS, AND FOLLOWING PRESENTERS TO
// INHERIT FROM WITH ALL THEIR DUPLICATION

// ALSO, WE MAY NEED TO REDO THE USER THING WE DID WITH IT BEING IN ONLY ONE FRAGMENT. WILL
// PROBABLY FIX ITSELF WHEN WE CAUSE THE 4 PRESENTERS TO INHERIT FROM THE SAME ONE

public class FeedPresenter {
    private static final int PAGE_SIZE = 10;
    private FeedPresenter.View view;
    private StatusService statusService;
    private UserService userService;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public interface View {
        void displayErrorMessage(String message);
        void setLoadingStatus(boolean value);
        void addStatuses(List<Status> statuses);
    }

    public FeedPresenter(View view) {
        this.view = view;
        userService = new UserService();
        statusService = new StatusService();
    }

    public void loadMoreItems(User user) throws MalformedURLException {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new FeedPresenter.GetFeedObserver());
        }
    }

    public void getUser(String userAliasString) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAliasString, new StoryPresenter.GetUserObserver());
    }

    public class GetFeedObserver implements ServiceObserver.GetItemsObserver {

        @Override
        public void handleSuccess(List statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastStatus = (statuses.size() > 0) ? (Status) statuses.get(statuses.size() - 1) : null;
            view.addStatuses(statuses);
            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }
}
