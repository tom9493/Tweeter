package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private MainPresenter.View view;
    private FollowService followService;
    private LoginService loginService;
    private StatusService statusService;

    public interface View extends ViewInterface {
        void displayFollowButton(boolean isFollower);
        void updateFollowing(int count);
        void updateFollowers(int count);
        void postSuccess(String message);
        void unfollow();
        void follow();
        void logout();
    }

    public MainPresenter(View view) {
        this.view = view;
        followService = getFollowService();
        statusService = getStatusService();
    }

    protected FollowService getFollowService() {
        if (followService == null) {
            followService = new FollowService();
        }
        return followService;
    }

    protected LoginService getLoginService() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerObserver());
    }

    public void unFollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new UnfollowObserver());
    }

    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new FollowObserver());

    }

    public void logout() {
        getLoginService().logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public void postStatus(String post) throws Exception {
        getStatusService().postStatus(post, Cache.getInstance().getCurrUser(),
                Cache.getInstance().getCurrUserAuthToken(), new PostStatusObserver());
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(),
        selectedUser, new GetFollowersCountObserver(), new GetFollowingCountObserver());
    }

    public User getCurrUser() { return Cache.getInstance().getCurrUser(); }
    public void clearCache() { Cache.getInstance().clearCache(); }

    public class IsFollowerObserver implements ServiceObserver.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.displayFollowButton(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }

    public class UnfollowObserver implements ServiceObserver.SuccessObserver {

        @Override
        public void handleSuccess() {
            view.unfollow();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }
    }

    public class FollowObserver implements ServiceObserver.SuccessObserver {

        @Override
        public void handleSuccess() {
            view.follow();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to follow because of exception: " + ex.getMessage());
        }
    }

    public class LogoutObserver implements ServiceObserver.SuccessObserver {

        @Override
        public void handleSuccess() {
            view.logout();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }

    public class PostStatusObserver implements ServiceObserver.SuccessObserver {

        @Override
        public void handleSuccess() {
            view.postSuccess("Successfully posted!");
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }

    public class GetFollowersCountObserver implements ServiceObserver.GetCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.updateFollowers(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }

    public class GetFollowingCountObserver implements ServiceObserver.GetCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.updateFollowing(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }

}
