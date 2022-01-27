package edu.byu.cs.tweeter.client.presenter;

import android.widget.ImageView;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {
    private RegisterPresenter.View view;
    private LoginService loginService;

    public interface View {
        void register(User registeredUser, String message);
        void displayErrorMessage(String message);
    }

    public RegisterPresenter(View view) {
        this.view = view;
        this.loginService = new LoginService();
    }

    public void register(String firstName, String lastName, String alias, String password, ImageView imageToUpload) {
        loginService.register(firstName, lastName, alias, password, imageToUpload, new RegisterPresenter.RegisterObserver());
    }

    public class RegisterObserver implements LoginService.RegisterObserver {

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.register(registeredUser, "Hello " + Cache.getInstance().getCurrUser().getName());
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}
