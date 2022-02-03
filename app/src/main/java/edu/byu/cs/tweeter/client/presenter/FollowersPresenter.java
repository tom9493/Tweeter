package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private FollowService followService;

    private static final int PAGE_SIZE = 10;
    private FollowersPresenter.View view;
    private UserService userService;
    private User lastFollower;
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
        void addFollowers(List<User> followers);
    }

    public FollowersPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new FollowersPresenter.GetFollowersObserver());
        }
    }

    public void getUser(String userAliasString) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAliasString, new StoryPresenter.GetUserObserver());
    }

    public class GetFollowersObserver implements ServiceObserver.GetItemsObserver {

        @Override
        public void handleSuccess(List followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastFollower = (followers.size() > 0) ? (User) followers.get(followers.size() - 1) : null;
            view.addFollowers(followers);
            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers because of exception: " + ex.getMessage());
        }
    }
}
