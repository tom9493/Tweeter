package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.net.MalformedURLException;
import java.util.List;

public class FeedPresenter extends PagedPresenter {
    private StatusService statusService;

    public FeedPresenter(View view) {
        super(view);
        statusService = new StatusService();
    }

    public void loadMoreItems(User user) throws MalformedURLException {
        if (!isLoading()) {
            setLoadInfo(true);

            statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user,
                    getPageSize(), (Status) getLastItem(), new FeedPresenter.GetFeedObserver());
        }
    }

    public class GetFeedObserver implements ServiceObserver.GetItemsObserver {

        @Override
        public void handleSuccess(List statuses, boolean hasMorePages) {
            setLoadInfo(false);
            setLastItem((statuses.size() > 0) ? (Status) statuses.get(statuses.size() - 1) : null);
            getView().addItems(statuses);
            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            setLoadInfo(false);
            getView().displayErrorMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            setLoadInfo(false);
            getView().displayErrorMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }
}
