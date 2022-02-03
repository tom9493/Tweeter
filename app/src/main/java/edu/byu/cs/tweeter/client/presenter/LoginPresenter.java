package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    private LoginPresenter.View view;
    private LoginService loginService;

    public interface View {
        void login(User loggedInUser, String message);
        void displayErrorMessage(String message);
    }

    public LoginPresenter(View view) {
        this.view = view;
        this.loginService = new LoginService();
    }

    public void login(String alias, String password) {
        loginService.login(alias, password, new LoginPresenter.LoginObserver());
    }

    public class LoginObserver implements ServiceObserver.LogRegObserver {

        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.login(loggedInUser, "Hello " + Cache.getInstance().getCurrUser().getName());
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
