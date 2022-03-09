package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

import java.io.IOException;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";
    static final String URL_PATH = "/login";

    private LoginRequest request;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
        this.request = new LoginRequest(username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {
        try {
            LoginResponse response = getServerFacade().login(request, URL_PATH);

            if(response.isSuccess()) {
                return new Pair<>(response.getUser(), response.getAuthToken());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to login", ex);
            sendExceptionMessage(ex);
        }
        return null;
    }

}
