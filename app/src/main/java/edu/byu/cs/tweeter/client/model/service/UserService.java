package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public void getUser(AuthToken currUserAuthToken, String userAliasString, StoryPresenter.GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAliasString, new GetUserHandler(getUserObserver));
        new ExecuteTask<>(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends TaskHandler {
        private final ServiceObserver.GetUserObserver observer;

        public GetUserHandler(ServiceObserver.GetUserObserver observer) {
            super(observer);
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.handleSuccess(user);
            } else { handleError(msg, GetUserTask.MESSAGE_KEY, GetUserTask.EXCEPTION_KEY); }
        }
    }
}
