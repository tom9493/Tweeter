package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.LoginService;
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

    public class LoginObserver implements LoginService.LoginObserver {

        @Override
        public void handleSuccess(User loggedInUser, String message) {
            view.login(loggedInUser, message);
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
