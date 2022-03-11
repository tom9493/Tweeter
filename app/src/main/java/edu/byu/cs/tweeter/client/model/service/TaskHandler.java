package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class TaskHandler<T extends ServiceObserver> extends Handler {
    public T observer;
    public TaskHandler(T observer) {
        super(Looper.getMainLooper());
        this.observer = observer; }

    public abstract class ItemsHandler extends TaskHandler {
        private final ServiceObserver.GetItemsObserver observer;
        public ItemsHandler(ServiceObserver.GetItemsObserver observer) {
            super(observer);
            this.observer = observer;
        }
        public void handlePageSuccess(@NonNull Message msg, String morePagesKey) {
            List items = (List) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(morePagesKey);
            observer.handleSuccess(items, hasMorePages);
        }
    }

    public abstract class SuccessHandler extends TaskHandler {
        private final ServiceObserver.SuccessObserver observer;
        public SuccessHandler(ServiceObserver.SuccessObserver observer) {
            super(observer);
            this.observer = observer;
        }
        public void handleSuccess() { observer.handleSuccess(); }
    }

    public abstract class CountHandler extends TaskHandler {
        private final ServiceObserver.GetCountObserver observer;
        public CountHandler(ServiceObserver.GetCountObserver observer) {
            super(observer);
            this.observer = observer;
        }
        public void handleCountSuccess(@NonNull Message msg, String countKey) {
            int count = msg.getData().getInt(countKey);
            observer.handleSuccess(count);
        }
    }

    public abstract class LogRegHandler extends TaskHandler {
        private final ServiceObserver.LogRegObserver observer;
        public LogRegHandler(ServiceObserver.LogRegObserver observer) {
            super(observer);
            this.observer = observer;
        }
        public void handleLoginSuccess(@NonNull Message msg, String userKey, String authTokenKey) {
            User loggedInUser = (User) msg.getData().getSerializable(userKey);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(authTokenKey);
            observer.handleSuccess(loggedInUser, authToken);
        }
    }

    public void handleError(@NonNull Message msg, String messageKey, String exceptionKey) {
        if (msg.getData().containsKey(messageKey)) {
            String message = msg.getData().getString(messageKey);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(exceptionKey)) {
            Exception ex = (Exception) msg.getData().getSerializable(exceptionKey);
            observer.handleException(ex);
        }
    }
}
