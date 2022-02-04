package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter {
    public StoryPresenter(View view) { super(view); }

    public void loadMoreItems(User user) throws MalformedURLException {
        if (!isLoading()) {
            setLoadInfo(true);
            getStatusService().getStory(Cache.getInstance().getCurrUserAuthToken(), user,
                    getPageSize(), (Status) getLastItem(), new StoryPresenter.GetStoryObserver());
        }
    }

    public void getUser(String userAliasString) {
        getUserService().getUser(Cache.getInstance().getCurrUserAuthToken(), userAliasString,
                new GetUserObserver());
    }

    public class GetStoryObserver implements ServiceObserver.GetItemsObserver {

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
            getView().displayErrorMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            setLoadInfo(false);
            getView().displayErrorMessage("Failed to get story because of exception: " + ex.getMessage());
        }
    }
}
