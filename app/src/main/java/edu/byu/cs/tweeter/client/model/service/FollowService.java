package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.*;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.client.presenter.FollowingPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, FollowingPresenter.GetFollowingObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(getFollowingObserver));
        new ExecuteTask<>(getFollowingTask);
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, FollowersPresenter.GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(getFollowersObserver));
        new ExecuteTask<>(getFollowersTask);
    }

    public void isFollower(AuthToken authToken, User currentUser, User selectedUser, MainPresenter.IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, currentUser, selectedUser,
                new IsFollowerHandler(isFollowerObserver));
        new ExecuteTask<>(isFollowerTask);
    }

    public void unfollow(AuthToken authToken, User currentUser, User selectedUser, MainPresenter.UnfollowObserver unfollowObserver){
        UnfollowTask unfollowTask = new UnfollowTask(authToken, currentUser, selectedUser,
                new UnfollowHandler(unfollowObserver));
        new ExecuteTask<>(unfollowTask);
    }

    public void follow(AuthToken authToken, User currentUser, User selectedUser, MainPresenter.FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(authToken, currentUser, selectedUser,
                new FollowHandler(followObserver));
        new ExecuteTask<>(followTask);
    }

    public void updateSelectedUserFollowingAndFollowers(AuthToken authToken, User selectedUser,
                                                        MainPresenter.GetFollowersCountObserver getFollowersCountObserver,
                                                        MainPresenter.GetFollowingCountObserver getFollowingCountObserver) {

        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken,
                selectedUser, new GetFollowersCountHandler(getFollowersCountObserver));
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken,
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        new ExecuteTask<>(followersCountTask, followingCountTask, 2);
    }

    private class GetFollowingHandler extends TaskHandler.ItemsHandler {
        public GetFollowingHandler(ServiceObserver.GetItemsObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) { handlePageSuccess(msg, GetFollowingTask.MORE_PAGES_KEY); }
            else { handleError(msg, GetFollowingTask.MESSAGE_KEY, GetFollowingTask.EXCEPTION_KEY); }
        }
    }

    private class GetFollowersHandler extends TaskHandler.ItemsHandler {
        public GetFollowersHandler(ServiceObserver.GetItemsObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) { handlePageSuccess(msg, GetFollowersTask.MORE_PAGES_KEY); }
            else { handleError(msg, GetFollowersTask.MESSAGE_KEY, GetFollowersTask.EXCEPTION_KEY); }
        }
    }

    private class IsFollowerHandler extends TaskHandler {
        private final ServiceObserver.IsFollowerObserver observer;
        private IsFollowerHandler(ServiceObserver.IsFollowerObserver observer) {
            super(observer);
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                observer.handleSuccess(isFollower);
            } else { handleError(msg, IsFollowerTask.MESSAGE_KEY, IsFollowerTask.EXCEPTION_KEY); }
        }
    }

    private class UnfollowHandler extends TaskHandler.SuccessHandler {
        private UnfollowHandler(ServiceObserver.SuccessObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) { handleSuccess(); }
            else { handleError(msg, UnfollowTask.MESSAGE_KEY, UnfollowTask.EXCEPTION_KEY); }
        }
    }

    private class FollowHandler extends TaskHandler.SuccessHandler {
        private FollowHandler(ServiceObserver.SuccessObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) { handleSuccess(); }
            else { handleError(msg, FollowTask.MESSAGE_KEY, FollowTask.EXCEPTION_KEY); }
        }
    }

    private class GetFollowersCountHandler extends TaskHandler.CountHandler {
        private GetFollowersCountHandler(ServiceObserver.GetCountObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) { handleCountSuccess(msg, GetFollowersCountTask.COUNT_KEY); }
            else { handleError(msg, GetFollowersCountTask.MESSAGE_KEY, GetFollowersCountTask.EXCEPTION_KEY);}
        }
    }

    private class GetFollowingCountHandler extends TaskHandler.CountHandler {
        private GetFollowingCountHandler(ServiceObserver.GetCountObserver observer) {
            new TaskHandler(observer).super(observer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) { handleCountSuccess(msg, GetFollowingCountTask.COUNT_KEY); }
            else { handleError(msg, GetFollowingCountTask.MESSAGE_KEY, GetFollowingCountTask.EXCEPTION_KEY);}
        }
    }
}
