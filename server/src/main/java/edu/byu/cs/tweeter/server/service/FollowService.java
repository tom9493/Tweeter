package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;
import edu.byu.cs.tweeter.server.Interface.AbstractFactory;
import edu.byu.cs.tweeter.server.Interface.FollowsDAOInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends PagedService {
    public FollowService(AbstractFactory factory) { super(factory); }

    public FollowingResponse getFollowing(FollowingRequest request) {
        try {
            validatePagedRequest(request.getUser().getAlias(), request.getLimit(), request.getAuthToken());

            List<User> allFollowees = new ArrayList<>();
            List<String> allFolloweeNames = getFollowsDAO().getFollowing(request.getUser().getAlias());

            for (String allFolloweeName : allFolloweeNames) {
                allFollowees.add(getUserDAO().getUser(allFolloweeName));
            }

            List<User> responseFollowees = new ArrayList<>(request.getLimit());
            boolean hasMorePages = false;

            if (request.getLimit() > 0) {
                if (allFollowees.size() != 0) {
                    int followeesIndex = getFolloweesStartingIndex(request.getLastUser(), allFollowees);
                    for (int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                        responseFollowees.add(allFollowees.get(followeesIndex));
                    }
                    hasMorePages = followeesIndex < allFollowees.size();
                }
            } else { return new FollowingResponse("limit was not > 0..."); }

            return new FollowingResponse(responseFollowees, hasMorePages);
        } catch(Exception ex) {
            return new FollowingResponse(ex.getMessage());
        }
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        try {
            validatePagedRequest(request.getUser().getAlias(), request.getLimit(), request.getAuthToken());

            List<User> allFollowers = new ArrayList<>();
            List<String> allFollowerNames = getFollowsDAO().getFollowers(request.getUser().getAlias());

            for (String allFollowerName : allFollowerNames) {
                allFollowers.add(getUserDAO().getUser(allFollowerName));
            }

            List<User> responseFollowers = new ArrayList<>(request.getLimit());
            boolean hasMorePages = false;

            if (request.getLimit() > 0) {
                if (allFollowers.size() != 0) {
                    int followeesIndex = getFolloweesStartingIndex(request.getLastUser(), allFollowers);

                    for (int limitCounter = 0; followeesIndex < allFollowers.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                        responseFollowers.add(allFollowers.get(followeesIndex));
                    }

                    hasMorePages = followeesIndex < allFollowers.size();
                }
            } else { return new FollowersResponse("limit was not > 0..."); }

            return new FollowersResponse(responseFollowers, hasMorePages);
        } catch (Exception ex) {
            return new FollowersResponse(ex.getMessage());
        }
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        try {
            validateAuthToken(request.getAuthToken());
            if (request.getFollower().getAlias() == null) {
                throw new RuntimeException("[BadRequest] IsFollower Request needs specified follower");
            } else if (request.getFollowee().getAlias() == null) {
                throw new RuntimeException("[BadRequest] IsFollower Request needs specified followee");
            }
            return new IsFollowerResponse(getFollowsDAO().isFollower(request.getFollower().getAlias(),
                    request.getFollowee().getAlias()));
        } catch (Exception ex) {
            return new IsFollowerResponse(ex.getMessage());
        }
    }

    public FollowResponse follow(FollowRequest request) {
        try {
            validateAuthToken(request.getAuthToken());
            if (request.getFollowee().getAlias() == null) {
                throw new RuntimeException("[BadRequest] Follow Request needs specified followee");
            }
            getFollowsDAO().follow(request.getFollower().getAlias(), request.getFollowee().getAlias());
            return new FollowResponse();
        } catch (Exception ex) {
            return new FollowResponse(ex.getMessage());
        }
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        try {
            validateAuthToken(request.getAuthToken());
            if (request.getFollowee().getAlias() == null) {
                throw new RuntimeException("[BadRequest] Unfollow Request needs specified followee");
            }
            getFollowsDAO().unfollow(request.getFollower().getAlias(), request.getFollowee().getAlias());
            return new UnfollowResponse();
        } catch (Exception ex) {
            return new UnfollowResponse(ex.getMessage());
        }
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        try {
            validateAuthToken(request.getAuthToken());
            if (request.getUserAlias() == null) {
                throw new RuntimeException("[BadRequest] Followers Count Request needs specified user alias");
            }
            return new FollowersCountResponse(getFollowsDAO().getFollowersCount(request.getUserAlias()));
        } catch (Exception ex) {
            return new FollowersCountResponse(ex.getMessage());
        }
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        try {
            validateAuthToken(request.getAuthToken());
            if (request.getUserAlias() == null) {
                throw new RuntimeException("[BadRequest] Following Count Request needs specified user alias");
            }
            return new FollowingCountResponse(getFollowsDAO().getFollowingCount(request.getUserAlias()));
        } catch (Exception ex) {
            return new FollowingCountResponse(ex.getMessage());
        }
    }

    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {
        int followeesIndex = 0;
        if(lastFollowee != null) {
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.equals(allFollowees.get(i))) {
                    followeesIndex = i + 1;
                    break;
                }
            }
        }
        return followeesIndex;
    }

    FollowsDAOInterface getFollowsDAO() {
        return factory.getFollowsDAO();
    }
}
