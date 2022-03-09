package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FollowingPresenter extends PagedPresenter {
    private FollowService followService;
    public FollowingPresenter(View view) { super(view); followService = new FollowService(); }

    public void loadMoreItems(User user) {
        if (!isLoading()) {
            setLoadInfo(true);

            followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user,
                    getPageSize(), (User) getLastItem(), new GetFollowingObserver());
        }
    }

    public class GetFollowingObserver implements ServiceObserver.GetItemsObserver {

        @Override
        public void handleSuccess(List followees, boolean hasMorePages) {
            setLoadInfo(false);
            setLastItem((followees.size() > 0) ? (User) followees.get(followees.size() - 1) : null);
            getView().addItems(followees);
            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            setLoadInfo(false);
            getView().displayErrorMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            setLoadInfo(false);
            getView().displayErrorMessage("Failed to get following because of exception: " + ex.getMessage());
        }
    }
}
