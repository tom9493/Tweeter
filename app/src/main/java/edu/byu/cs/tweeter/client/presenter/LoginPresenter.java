package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends LogRegPresenter {
    public LoginPresenter(View view) { super(view); }

    public void login(String alias, String password) {
        getLoginService().login(alias, password, new LoginPresenter.LoginObserver());
    }

    public class LoginObserver implements ServiceObserver.LogRegObserver {

        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            setCacheValues(loggedInUser, authToken);
            getView().login(loggedInUser, "Hello " + Cache.getInstance().getCurrUser().getName());
        }

        @Override
        public void handleFailure(String message) {
            getView().displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            getView().displayErrorMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
