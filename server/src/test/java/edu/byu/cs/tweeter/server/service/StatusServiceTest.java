package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.Interface.AbstractFactory;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class StatusServiceTest {
    private StoryRequest request;
    private AbstractFactory mockFactory;
    private StatusService statusServiceSpy;
    private StoryDAO mockStoryDAO;

    @Before
    public void setup() {
        mockFactory = Mockito.mock(AbstractFactory.class);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockFactory.getStoryDAO()).thenReturn(mockStoryDAO);
        statusServiceSpy = Mockito.spy(StatusService.class);
        statusServiceSpy.setFactory(mockFactory);
    }

    @Test
    public void testGetStory1_validRequest_correctResponse() {
        User user1 = new User("Wolf", "Profile", "@Wolf", "https://cs340s3.s3.us-west-1.amazonaws.com/%40Wolf_photo.png");
        request = new StoryRequest(null, user1, 7, null);

        // Fake story to be returned
        Status status = new Status("This is the status", user1, 0, null, null);
        List<Status> story = new ArrayList<>();
        story.add(status);
        StoryResponse expectedResponse = new StoryResponse(story, false);

        // When mockStoryDAO calls story with given parameters, returns expected story
        Mockito.when(mockStoryDAO.getStory(request.getUser(), request.getLimit(),
                        request.getLastStatus())).thenReturn(story);

        StoryResponse response = statusServiceSpy.getStory(request);

        Mockito.verify(mockFactory).getStoryDAO();
        Mockito.verify(mockStoryDAO).getStory(request.getUser(), request.getLimit(), request.getLastStatus());
        Mockito.verify(statusServiceSpy).getStory(request);

        Assert.assertEquals(expectedResponse, response);
        Assert.assertEquals(1, response.getStory().size());
        Assert.assertEquals(response.getStory().get(0).getUser(), user1);
    }

//    @Test
//    public void testGetStory2_validRequest_correctResponse() {
//        User user2 = new User("Lion", "Profile", "@Lion", "https://cs340s3.s3.us-west-1.amazonaws.com/%40Lion_photo.png");
//        request = new StoryRequest(null, user2, 7, null);
//
//        StoryResponse response = statusServiceSpy.getStory(request);
//        Mockito.verify(mockFactory).getStoryDAO();
//        Mockito.verify(statusServiceSpy).getStory(request);
//        Assert.assertNotNull(response.getStory());
//        Assert.assertEquals(7, response.getStory().size());
//
//        // Affirm each status returned was a post from correct user
//        for (int i = 0; i < response.getStory().size(); ++i) {
//            Assert.assertEquals(response.getStory().get(0).getUser(), user2);
//        }
//
//        request.setLastStatus(response.getStory().get(6));
//        request.setLimit(20);
//
//        response = statusServiceSpy.getStory(request);
//        Mockito.verify(mockFactory, times(2)).getStoryDAO();
//        Mockito.verify(statusServiceSpy, times(2)).getStory(request);
//        Assert.assertNotNull(response.getStory());
//        Assert.assertEquals(4, response.getStory().size()); // Only 15 statuses, so shouldn't be size 20
//
//        // Affirm each status returned was a post from correct user
//        for (int i = 0; i < response.getStory().size(); ++i) {
//            Assert.assertEquals(response.getStory().get(0).getUser(), user2);
//        }
//    }
//
//    @Test
//    public void testGetStory3_validRequest_correctResponse() {
//        User user3 = new User("Ox", "Profile", "@Ox", "https://cs340s3.s3.us-west-1.amazonaws.com/%40Ox_photo.png");
//        request = new StoryRequest(null, user3, 7, null);
//
//        StoryResponse response = statusServiceSpy.getStory(request);
//        Mockito.verify(mockFactory).getStoryDAO();
//        Mockito.verify(statusServiceSpy).getStory(request);
//        Assert.assertNotNull(response.getStory());
//        Assert.assertEquals(7, response.getStory().size());
//
//        // Affirm each status returned was a post from correct user
//        for (int i = 0; i < response.getStory().size(); ++i) {
//            Assert.assertEquals(response.getStory().get(0).getUser(), user3);
//        }
//
//        request.setLastStatus(response.getStory().get(6));
//        request.setLimit(20);
//
//        response = statusServiceSpy.getStory(request);
//        Mockito.verify(mockFactory, times(2)).getStoryDAO();
//        Mockito.verify(statusServiceSpy, times(2)).getStory(request);
//        Assert.assertNotNull(response.getStory());
//        Assert.assertEquals(1, response.getStory().size()); // Only 15 statuses, so shouldn't be size 20
//
//        // Affirm each status returned was a post from correct user
//        for (int i = 0; i < response.getStory().size(); ++i) {
//            Assert.assertEquals(response.getStory().get(0).getUser(), user3);
//        }
//    }
}
