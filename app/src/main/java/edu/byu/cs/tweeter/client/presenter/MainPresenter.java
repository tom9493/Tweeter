package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private MainPresenter.View view;
    private FollowService followService;
    private LoginService loginService;

    public interface View {
        void displayErrorMessage(String message);
        void displayFollowButton(boolean isFollower);
        void unfollow();
        void follow();
        void logout();
    }

    public MainPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        loginService = new LoginService();
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerObserver());
    }

    public void unFollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new UnfollowObserver());
    }

    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new FollowObserver());

    }

    public void logout() {
        loginService.logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public User getCurrUser() { return Cache.getInstance().getCurrUser(); }

    public void clearCache() { Cache.getInstance().clearCache(); }

    public class IsFollowerObserver implements FollowService.IsFollowerObserver {

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

    public class UnfollowObserver implements FollowService.UnfollowObserver {

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

    public class FollowObserver implements FollowService.FollowObserver {

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

    public class LogoutObserver implements LoginService.LogoutObserver {

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

}
