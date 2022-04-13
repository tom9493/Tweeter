package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StatusService {

    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, ServiceObserver.GetItemsObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        new ExecuteTask<>(getStoryTask);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, ServiceObserver.GetItemsObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        new ExecuteTask<>(getFeedTask);
    }

    public void postStatus(String post, User currentUser, AuthToken authToken, ServiceObserver.SuccessObserver postStatusObserver) throws Exception {
        Status newStatus = new Status(post, currentUser, System.currentTimeMillis(), parseURLs(post), parseMentions(post));
        PostStatusTask statusTask = new PostStatusTask(authToken, newStatus, System.currentTimeMillis(),
                new PostStatusHandler(postStatusObserver));
        new ExecuteTask<>(statusTask);
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) throws MalformedURLException {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {
                int index = findUrlEndIndex(word);
                word = word.substring(0, index);
                containedUrls.add(word);
            }
        }
        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);
                containedMentions.add(word);
            }
        }
        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    private class GetStoryHandler extends TaskHandler.ItemsHandler {
        public GetStoryHandler(ServiceObserver.GetItemsObserver observer) {
            new TaskHandler(observer).super(observer);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) { handlePageSuccess(msg, GetStoryTask.MORE_PAGES_KEY); }
            else { handleError(msg, GetStoryTask.MESSAGE_KEY, GetStoryTask.EXCEPTION_KEY); }
        }
    }

    private class GetFeedHandler extends TaskHandler.ItemsHandler {
        public GetFeedHandler(ServiceObserver.GetItemsObserver observer) {
            new TaskHandler(observer).super(observer);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) { handlePageSuccess(msg, GetFeedTask.MORE_PAGES_KEY); }
            else { handleError(msg, GetFeedTask.MESSAGE_KEY, GetFeedTask.EXCEPTION_KEY); }
        }
    }

    private class PostStatusHandler extends TaskHandler.SuccessHandler {
        private PostStatusHandler(ServiceObserver.SuccessObserver observer) {
            new TaskHandler(observer).super(observer);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
            if (success) { handleSuccess(); }
            else { handleError(msg, PostStatusTask.MESSAGE_KEY, PostStatusTask.EXCEPTION_KEY); }
        }
    }
}
