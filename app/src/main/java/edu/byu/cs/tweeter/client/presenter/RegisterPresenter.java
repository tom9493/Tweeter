package edu.byu.cs.tweeter.client.presenter;

import android.widget.ImageView;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends LogRegPresenter {
    public RegisterPresenter(View view) { super(view); }

    public void register(String firstName, String lastName, String alias, String password, ImageView imageToUpload) {
        getLoginService().register(firstName, lastName, alias, password, imageToUpload, new RegisterPresenter.RegisterObserver());
    }

    public class RegisterObserver implements ServiceObserver.LogRegObserver {

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            setCacheValues(registeredUser, authToken);
            getView().login(registeredUser, "Hello " + Cache.getInstance().getCurrUser().getName());
        }

        @Override
        public void handleFailure(String message) {
            getView().displayErrorMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            getView().displayErrorMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}
