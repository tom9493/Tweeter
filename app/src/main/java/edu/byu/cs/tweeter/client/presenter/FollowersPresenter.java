package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter {
    public FollowersPresenter(View view) { super(view); }

    public void loadMoreItems(User user) {
        if (!isLoading()) {
            setLoadInfo(true);
            getFollowService().getFollowers(Cache.getInstance().getCurrUserAuthToken(), user,
                    getPageSize(), (User) getLastItem(), new FollowersPresenter.GetFollowersObserver());
        }
    }

    public class GetFollowersObserver implements ServiceObserver.GetItemsObserver {

        @Override
        public void handleSuccess(List followers, boolean hasMorePages) {
            setLoadInfo(false);
            setLastItem((followers.size() > 0) ? (User) followers.get(followers.size() - 1) : null);
            getView().addItems(followers);
            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            setLoadInfo(false);
            getView().displayErrorMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            setLoadInfo(false);
            getView().displayErrorMessage("Failed to get followers because of exception: " + ex.getMessage());
        }
    }
}
