package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.Pair;

import java.io.IOException;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticateTask {

    private static final String LOG_TAG = "RegisterTask";
    static final String URL_PATH = "/register";

    private RegisterRequest request;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler, username, password);
        this.request = new RegisterRequest(firstName, lastName, username, password, image);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {
        try {
            RegisterResponse response = getServerFacade().register(request, URL_PATH);

            if(response.isSuccess()) {
                return new Pair<>(response.getUser(), response.getAuthToken());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to register", ex);
            sendExceptionMessage(ex);
        }
        return null;
    }
}
