package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.client.presenter.FollowingPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    // Tasks

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, FollowingPresenter.GetFollowingObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(getFollowingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, FollowersPresenter.GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(getFollowersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void isFollower(AuthToken authToken, User currentUser, User selectedUser, MainPresenter.IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, currentUser, selectedUser,
                new IsFollowerHandler(isFollowerObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(isFollowerTask);
    }

    public void unfollow(AuthToken authToken, User selectedUser, MainPresenter.UnfollowObserver unfollowObserver){
        UnfollowTask unfollowTask = new UnfollowTask(authToken, selectedUser,
                new UnfollowHandler(unfollowObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    public void follow(AuthToken authToken, User selectedUser, MainPresenter.FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(authToken, selectedUser,
                new FollowHandler(followObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void updateSelectedUserFollowingAndFollowers(AuthToken authToken, User selectedUser,
                                                        MainPresenter.GetFollowersCountObserver getFollowersCountObserver,
                                                        MainPresenter.GetFollowingCountObserver getFollowingCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                selectedUser, new GetFollowersCountHandler(getFollowersCountObserver));
        executor.execute(followersCountTask);

        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        executor.execute(followingCountTask);
    }

    // Observers

    public interface GetFollowingObserver {
        void handleSuccess(List<User> followees, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public interface GetFollowersObserver {
        void handleSuccess(List<User> followers, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public interface IsFollowerObserver {
        void handleSuccess(boolean isFollower);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public interface FollowObserver {
        void handleSuccess();
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public interface UnfollowObserver {
        void handleSuccess();
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public interface GetFollowersCountObserver {
        void handleSuccess(int count);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public interface GetFollowingCountObserver {
        void handleSuccess(int count);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    // Handlers

    private class GetFollowingHandler extends Handler {
        private final GetFollowingObserver observer;

        public GetFollowingHandler(GetFollowingObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.handleSuccess(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.handleFailure(message);

            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.handleException(ex);

            }
        }
    }

    private class GetFollowersHandler extends Handler {
        private final GetFollowersObserver observer;

        public GetFollowersHandler(GetFollowersObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                observer.handleSuccess(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    private class IsFollowerHandler extends Handler {
        private final IsFollowerObserver observer;

        private IsFollowerHandler(IsFollowerObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.handleSuccess(isFollower);
            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    private class UnfollowHandler extends Handler {
        private final UnfollowObserver observer;

        private UnfollowHandler(UnfollowObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                observer.handleSuccess();
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    private class FollowHandler extends Handler {
        private final FollowObserver observer;

        private FollowHandler(FollowObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                observer.handleSuccess();
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    private class GetFollowersCountHandler extends Handler {
        private final GetFollowersCountObserver observer;

        private GetFollowersCountHandler(GetFollowersCountObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                observer.handleSuccess(count);
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    private class GetFollowingCountHandler extends Handler {
        private final GetFollowingCountObserver observer;

        private GetFollowingCountHandler(GetFollowingCountObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                observer.handleSuccess(count);
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }
}
