package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {
    private RegisterPresenter.View view;
    private RegisterService registerService;

    public interface View {
        void register(User registeredUser, String message);
        void displayErrorMessage(String message);
    }

    public RegisterPresenter(View view) {
        this.view = view;
        this.registerService = new RegisterService();
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        registerService.register(firstName, lastName, alias, password, imageBytesBase64, new RegisterPresenter.RegisterObserver());
    }

    public class RegisterObserver implements RegisterService.RegisterObserver {

        @Override
        public void handleSuccess(User registeredUser, String message) {
            view.register(registeredUser, message);
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
