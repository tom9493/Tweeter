package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public abstract class PagedPresenter<T> {
    private static final int PAGE_SIZE = 10;
    private PagedPresenter.View view;
    private UserService userService;
    private FollowService followService;
    private StatusService statusService;
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public PagedPresenter(View view) {
        this.view = view;
        userService = new UserService();
        statusService = new StatusService();
        followService = new FollowService();
    }

    public static int getPageSize() {
        return PAGE_SIZE;
    }

    public PagedPresenter.View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public UserService getUserService() {
        return userService;
    }

    public FollowService getFollowService() {
        return followService;
    }

    public StatusService getStatusService() {
        return statusService;
    }

    public T getLastItem() {
        return lastItem;
    }

    public void setLastItem(T lastItem) { this.lastItem = lastItem; }

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

    public void setLoadInfo(boolean val) {
        setLoading(val);
        getView().setLoadingStatus(val);
    }

    public interface View<T> extends ViewInterface {
        void setLoadingStatus(boolean value);
        void addItems(List<T> items);
        void getUserPage(User user);
    }

    public void getUser(String userAliasString) {
        getUserService().getUser(Cache.getInstance().getCurrUserAuthToken(), userAliasString,
                new GetUserObserver());
    }

    public class GetUserObserver implements ServiceObserver.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.getUserPage(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }
    }
}
