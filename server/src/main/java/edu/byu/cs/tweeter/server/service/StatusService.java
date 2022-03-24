package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.Interface.AbstractFactory;
import edu.byu.cs.tweeter.server.Interface.FeedDAOInterface;
import edu.byu.cs.tweeter.server.Interface.FollowsDAOInterface;
import edu.byu.cs.tweeter.server.Interface.StoryDAOInterface;

import java.util.ArrayList;
import java.util.List;

public class StatusService extends PagedService {
    public StatusService(AbstractFactory factory) { super(factory); }

    public FeedResponse getFeed(FeedRequest request) {
        try {
            validatePagedRequest(request.getUser().getAlias(), request.getLimit(), request.getAuthToken());

            List<Status> allStatuses = factory.getFeedDAO().getFeed(request.getUser(), request.getLimit(), request.getLastStatus()); // getFeed

            for (int i = 0; i < allStatuses.size(); ++i) { // These statuses have a null user object. Populate them?
                allStatuses.get(i).setUser(getUserDAO().getUser(allStatuses.get(i).getSenderAlias()));
            }

            List<Status> responseStatuses = new ArrayList<>(request.getLimit());
            boolean hasMorePages = false;

            if (allStatuses != null) {
                int statusIndex = getStatusesStartingIndex(request.getLastStatus(), allStatuses);
                for (int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusIndex));
                }
                hasMorePages = statusIndex < allStatuses.size();
            }
            return new FeedResponse(responseStatuses, hasMorePages);

        } catch (Exception ex) {
            return new FeedResponse(ex.getMessage());
        }
    }

    public StoryResponse getStory(StoryRequest request) {
        try {
            validatePagedRequest(request.getUser().getAlias(), request.getLimit(), request.getAuthToken());

            List<Status> allStatuses = factory.getStoryDAO().getStory(request.getUser(), request.getLimit(), request.getLastStatus());
            List<Status> responseStatuses = new ArrayList<>(request.getLimit());
            boolean hasMorePages = false;

            if (allStatuses != null) {
                int statusIndex = getStatusesStartingIndex(request.getLastStatus(), allStatuses);
                for (int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusIndex));
                }
                hasMorePages = statusIndex < allStatuses.size();
            }
            return new StoryResponse(responseStatuses, hasMorePages);

        } catch (Exception ex) {
            return new StoryResponse(ex.getMessage());
        }
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        try {
            validateAuthToken(request.getAuthToken());
            if (request.getStatus() == null) {
                throw new RuntimeException("[BadRequest] Post Status Request needs valid status");
            }
            List<String> followers = getFollowsDAO().getFollowers(request.getStatus().getUser().getAlias());
            for (String follower : followers) {
                getFeedDAO().addStatusToFeed(follower, request.getStatus());
            }
            getStoryDAO().addStatusToStory(request.getStatus());
            return new PostStatusResponse();
        } catch (Exception ex) {
            return new PostStatusResponse(ex.getMessage());
        }
    }

    private int getStatusesStartingIndex(Status lastStatus, List<Status> allStatuses) {
        int statusIndex = 0;
        if(lastStatus != null) {
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    statusIndex = i + 1;
                    break;
                }
            }
        }
        return statusIndex;
    }

    FeedDAOInterface getFeedDAO() { return factory.getFeedDAO(); }
    StoryDAOInterface getStoryDAO() { return factory.getStoryDAO(); }
    FollowsDAOInterface getFollowsDAO() { return factory.getFollowsDAO(); }
}
