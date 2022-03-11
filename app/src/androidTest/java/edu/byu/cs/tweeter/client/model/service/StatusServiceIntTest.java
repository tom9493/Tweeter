package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class StatusServiceIntTest {
    private User currentUser;
    private AuthToken currentAuthToken;

    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;

    private CountDownLatch countDownLatch;

    @Before
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken("token", "datetime");

        statusServiceSpy = Mockito.spy(new StatusService());

        observer = new StatusServiceObserver();
        resetCountDownLatch();
    }

    private void resetCountDownLatch() { countDownLatch = new CountDownLatch(1); }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    private class StatusServiceObserver implements ServiceObserver.GetItemsObserver {

        private boolean success;
        private String message;
        private List<Status> feed;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List feed, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.feed = feed;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {

        }

        @Override
        public void handleException(Exception exception) {
            System.out.println(exception.getMessage());
        }

        public boolean isSuccess() { return success; }

        public String getMessage() { return message; }

        public List<Status> getFeed() { return feed; }

        public boolean hasMorePages() { return hasMorePages; }

        public Exception getException() { return exception; }
    }

    @Test
    public void testGetFeed_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.getFeed(currentAuthToken, currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = new FakeData().getFakeStatuses().subList(0,3);
        List<Status> returnedStatuses = observer.getFeed();
        Assert.assertTrue(observer.isSuccess());
        Assert.assertNull(observer.getMessage());
        for (int i = 0; i < 2; ++i) {
            Assert.assertEquals(expectedStatuses.get(i).getMentions(), returnedStatuses.get(i).getMentions());
            Assert.assertEquals(expectedStatuses.get(i).getPost(), returnedStatuses.get(i).getPost());
            Assert.assertEquals(expectedStatuses.get(i).getUrls(), returnedStatuses.get(i).getUrls());
            Assert.assertEquals(expectedStatuses.get(i).getUser(), returnedStatuses.get(i).getUser());
        }
        Assert.assertTrue(observer.hasMorePages());
        Assert.assertNull(observer.getException());
    }

    @Test
    public void testGetFeed_validRequest_loadsStatuses() throws InterruptedException {
        statusServiceSpy.getFeed(currentAuthToken, currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> feed = observer.getFeed();
        Assert.assertTrue(feed.size() > 0);
    }

}