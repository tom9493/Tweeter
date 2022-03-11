package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusService {
    public FeedResponse getFeed(FeedRequest request) {
        if (!request.getAuthToken().equals(getDummyAuthToken())) {
            throw new RuntimeException("[BadRequest] User Request needs valid authToken");
        } else if (request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Feed Request needs specified user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Feed Request needs to have a positive limit");
        }
        return getStatusDAO().getFeed(request);
    }

    public StoryResponse getStory(StoryRequest request) {
        if (!request.getAuthToken().equals(getDummyAuthToken())) {
            throw new RuntimeException("[BadRequest] User Request needs valid authToken");
        } else if (request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Story Request needs specified user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Story Request needs to have a positive limit");
        }
        return getStatusDAO().getStory(request);
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (!request.getAuthToken().equals(getDummyAuthToken())) {
            throw new RuntimeException("[BadRequest] User Request needs valid authToken");
        } else if (request.getStatus() == null) {
            throw new RuntimeException("[BadRequest] Post Status Request needs valid status");
        }
        return getStatusDAO().postStatus(request);
    }

    public StatusDAO getStatusDAO() { return new StatusDAO(); }

    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
