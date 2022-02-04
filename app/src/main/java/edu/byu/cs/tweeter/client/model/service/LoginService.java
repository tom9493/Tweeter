package edu.byu.cs.tweeter.client.model.service;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LoginService {

    public void login(String alias, String password, LoginPresenter.LoginObserver loginObserver) {
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(loginObserver));
        new ExecuteTask<>(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password, ImageView imageToUpload, RegisterPresenter.RegisterObserver registerObserver) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterHandler(registerObserver));

        new ExecuteTask<>(registerTask);
    }

    public void logout(AuthToken authToken, MainPresenter.LogoutObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(authToken, new LogoutHandler(logoutObserver));
        new ExecuteTask<>(logoutTask);
    }

    private class LoginHandler extends TaskHandler.LogRegHandler {
        public LoginHandler(ServiceObserver.LogRegObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) { handleLoginSuccess(msg, LoginTask.USER_KEY, LoginTask.AUTH_TOKEN_KEY); }
            else { handleError(msg, LoginTask.MESSAGE_KEY, LoginTask.EXCEPTION_KEY);}
        }
    }

    private class LogoutHandler extends TaskHandler.SuccessHandler {
        private LogoutHandler(ServiceObserver.SuccessObserver observer) {
            new TaskHandler(observer).super(observer);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) { handleSuccess(); }
            else { handleError(msg, LogoutTask.MESSAGE_KEY, LogoutTask.EXCEPTION_KEY); }
        }
    }

    private class RegisterHandler extends TaskHandler.LogRegHandler {
        public RegisterHandler(ServiceObserver.LogRegObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) { handleLoginSuccess(msg, RegisterTask.USER_KEY, RegisterTask.AUTH_TOKEN_KEY); }
            else { handleError(msg, RegisterTask.MESSAGE_KEY, RegisterTask.EXCEPTION_KEY);}
        }
    }
}
