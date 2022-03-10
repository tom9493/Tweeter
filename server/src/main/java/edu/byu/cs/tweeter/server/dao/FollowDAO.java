package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;
import edu.byu.cs.tweeter.util.FakeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAO {

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert request.getUserAlias() != null;
        return new FollowingCountResponse(getDummyFollowees().size());
    }

    public FollowersCountResponse getFollowerCount(FollowersCountRequest request) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert request.getUserAlias() != null;
        return new FollowersCountResponse(getDummyFollowees().size());
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        Random rd = new Random();
        return new IsFollowerResponse(rd.nextBoolean());
    }

    public FollowResponse follow(FollowRequest request) {
        return new FollowResponse(); // Follow Response with no parameters defaults to true/success
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        return new UnfollowResponse();
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUser() != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastUser(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUser() != null;

        List<User> allFollowees = getDummyFollowees();
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastUser(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowers.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowersResponse(responseFollowers, hasMorePages);
    }

    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee == allFollowees.get(i)) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}