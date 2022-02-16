package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

import java.io.Serializable;
import java.util.List;

public interface ServiceObserver {
    void handleFailure(String message);
    void handleException(Exception exception);

    interface LogRegObserver extends ServiceObserver {
        void handleSuccess(User user, AuthToken authToken);
    }

    interface IsFollowerObserver extends ServiceObserver {
        void handleSuccess(boolean isFollower);
    }

    interface SuccessObserver extends ServiceObserver {
        void handleSuccess();
    }

    interface GetCountObserver extends ServiceObserver {
        void handleSuccess(int count);
    }

    interface GetUserObserver extends ServiceObserver {
        void handleSuccess(User user);
    }

    interface GetItemsObserver<T extends Serializable> extends ServiceObserver {
        void handleSuccess(List<T> statuses, boolean hasMorePages);
    }
}


