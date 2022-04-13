package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ComplexIntTest {
    private LoginService loginServiceSpy;
    private StatusService statusServiceSpy;
    private MainPresenter.View mainPresenterViewMock;
    private StatusServiceObserver statusObserver;
    private LoginServiceObserver loginObserver;
    private PostStatusObserver postStatusObserver;
    private CountDownLatch countDownLatch;

    @Before
    public void setup() {
        mainPresenterViewMock = Mockito.mock(MainPresenter.View.class);
        statusServiceSpy = Mockito.spy(StatusService.class);
        loginServiceSpy = Mockito.spy(LoginService.class);

        statusObserver = new StatusServiceObserver();
        loginObserver = new LoginServiceObserver();
        postStatusObserver = new PostStatusObserver();
        resetCountDownLatch();
    }

    private void resetCountDownLatch() { countDownLatch = new CountDownLatch(1); }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void complexTest_validRequests_correctResponses() throws Exception {
        loginServiceSpy.login("@Cow", "cow", loginObserver);
        awaitCountDownLatch();

        Assert.assertTrue(loginObserver.isSuccess());
        Assert.assertNull(loginObserver.getMessage());
        Assert.assertNull(loginObserver.getException());
        Assert.assertNotNull(loginObserver.getAuthToken());
        Mockito.verify(loginServiceSpy).login("@Cow", "cow", loginObserver);

        statusServiceSpy.postStatus("This is my post!", loginObserver.getUser(), loginObserver.getAuthToken(),
                postStatusObserver);
        awaitCountDownLatch();

        Assert.assertTrue(postStatusObserver.isSuccess());
        Assert.assertNull(postStatusObserver.getMessage());
        Assert.assertNull(postStatusObserver.getException());
        Mockito.verify(statusServiceSpy).postStatus("This is my post!", loginObserver.getUser(), loginObserver.getAuthToken(),
                postStatusObserver);
        Mockito.verify(mainPresenterViewMock).postSuccess("Successfully Posted!");

        statusServiceSpy.getStory(loginObserver.getAuthToken(), loginObserver.getUser(), 3, null, statusObserver);
        awaitCountDownLatch();

        Assert.assertTrue(statusObserver.isSuccess());
        Assert.assertNull(statusObserver.getMessage());
        Assert.assertNull(statusObserver.getException());
        Mockito.verify(statusServiceSpy).getStory(loginObserver.getAuthToken(), loginObserver.getUser(), 3, null, statusObserver);

        int size = statusObserver.getStory().size();
        Assert.assertEquals("This is my post!", statusObserver.getStory().get(0).getPost());
        Assert.assertEquals(loginObserver.getUser(), statusObserver.getStory().get(0).getUser());
    }

    // Gets story
    private class StatusServiceObserver implements ServiceObserver.GetItemsObserver {
        private boolean success = false;
        private String message;
        private List<Status> story;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List story, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = story;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) { countDownLatch.countDown(); }
        @Override
        public void handleException(Exception exception) {
            this.message = exception.getMessage();
            countDownLatch.countDown();
            System.out.println(message);
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public List<Status> getStory() { return story; }
        public boolean hasMorePages() { return hasMorePages; }
        public Exception getException() { return exception; }
    }

    // Login
    private class LoginServiceObserver implements ServiceObserver.LogRegObserver {
        private boolean success = false;
        private String message;
        private AuthToken authToken;
        private User user;
        private Exception exception;

        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            this.success = true;
            this.message = null;
            this.authToken = authToken;
            this.user = loggedInUser;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) { countDownLatch.countDown(); }
        @Override
        public void handleException(Exception exception) {
            this.message = exception.getMessage();
            countDownLatch.countDown();
            System.out.println(message);
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public AuthToken getAuthToken() { return authToken; }
        public User getUser() { return user; }
        public Exception getException() { return exception; }
    }

    // Post status
    private class PostStatusObserver implements ServiceObserver.SuccessObserver {
        private boolean success = false;
        private String message;
        private Exception exception;

        @Override
        public void handleSuccess() {
            this.success = true;
            this.message = null;
            this.exception = null;
            mainPresenterViewMock.postSuccess("Successfully Posted!");
            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) { countDownLatch.countDown(); }
        @Override
        public void handleException(Exception exception) {
            this.message = exception.getMessage();
            countDownLatch.countDown();
            System.out.println(message);
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Exception getException() { return exception; }
    }
}