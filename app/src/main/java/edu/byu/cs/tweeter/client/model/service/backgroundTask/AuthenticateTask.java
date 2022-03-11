package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

import java.io.IOException;

public abstract class AuthenticateTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    private User authenticatedUser;

    private AuthToken authToken;

    protected final String username;
    protected final String password;

    protected AuthenticateTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }


    @Override
    protected final void runTask()  throws IOException {
        Pair<User, AuthToken> loginResult = runAuthenticationTask();

        if (loginResult != null) {
            authenticatedUser = loginResult.getFirst();
            authToken = loginResult.getSecond();
            Cache.getInstance().setCurrUserAuthToken(authToken);
            System.out.println("currUserAuthToken: " + Cache.getInstance().getCurrUserAuthToken().getToken());

            sendSuccessMessage();
        }
        // Fail message sent in specific task
    }

    protected abstract Pair<User, AuthToken> runAuthenticationTask();

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
