package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
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
    private StatusServiceObserver statusObserver;
    private LoginServiceObserver loginObserver;
    private PostStatusObserver postStatusObserver;
    private CountDownLatch countDownLatch;

    @Before
    public void setup() {
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
        loginServiceSpy.login("@Wolf", "wolf", loginObserver);
        awaitCountDownLatch();

        statusServiceSpy.postStatus("This is my post!", loginObserver.getUser(), loginObserver.getAuthToken(),
                postStatusObserver);
        awaitCountDownLatch();

        statusServiceSpy.getStory(loginObserver.getAuthToken(), loginObserver.getUser(), 3, null, statusObserver);
        awaitCountDownLatch();

        Mockito.verify(loginServiceSpy).login("@Wolf", "wolf", loginObserver);
        Mockito.verify(statusServiceSpy).postStatus("This is my post!", loginObserver.getUser(), loginObserver.getAuthToken(),
                postStatusObserver);
        Mockito.verify(statusServiceSpy).getStory(loginObserver.getAuthToken(), loginObserver.getUser(), 3, null, statusObserver);
        awaitCountDownLatch();

        // All succeeded
        Assert.assertTrue(loginObserver.isSuccess());
        Assert.assertTrue(statusObserver.isSuccess());
        Assert.assertTrue(postStatusObserver.isSuccess());
        Assert.assertNull(loginObserver.getMessage());
        Assert.assertNull(statusObserver.getMessage());
        Assert.assertNull(postStatusObserver.getMessage());
        Assert.assertNull(loginObserver.getException());
        Assert.assertNull(statusObserver.getException());
        Assert.assertNull(postStatusObserver.getException());

        Assert.assertNotNull(loginObserver.getAuthToken());
        Assert.assertEquals("This is my post!", statusObserver.getStory().get(0).getPost());
        Assert.assertEquals(loginObserver.getUser(), statusObserver.getStory().get(0).getUser());
    }

    // Observers for tests
    private class StatusServiceObserver implements ServiceObserver.GetItemsObserver {
        private boolean success;
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
        public void handleFailure(String message) {}
        @Override
        public void handleException(Exception exception) {
            this.message = exception.getMessage();
            System.out.println(message);
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public List<Status> getStory() { return story; }
        public boolean hasMorePages() { return hasMorePages; }
        public Exception getException() { return exception; }
    }

    private class LoginServiceObserver implements ServiceObserver.LogRegObserver {
        private boolean success;
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
        public void handleFailure(String message) { }
        @Override
        public void handleException(Exception exception) {
            this.message = exception.getMessage();
            System.out.println(message);
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public AuthToken getAuthToken() { return authToken; }
        public User getUser() { return user; }
        public Exception getException() { return exception; }
    }

    private class PostStatusObserver implements ServiceObserver.SuccessObserver {
        private boolean success;
        private String message;
        private Exception exception;

        @Override
        public void handleSuccess() {
            this.success = true;
            this.message = null;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) { }
        @Override
        public void handleException(Exception exception) {
            this.message = exception.getMessage();
            System.out.println(message);
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Exception getException() { return exception; }
    }
}