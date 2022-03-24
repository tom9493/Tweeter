package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import edu.byu.cs.tweeter.server.Interface.FollowsDAOInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowsDAO implements FollowsDAOInterface {
    protected static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-1").build();
    protected static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
    private final Table table = dynamoDB.getTable("Follows");
    private final Index index = table.getIndex("secondary");
    private final String partKey = "follower_handle";
    private final String sortKey = "followee_handle";

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        Item item = table.getItem(partKey, followerAlias, sortKey, followeeAlias);
        return item != null;
    }

    @Override
    public List<String> getFollowers(String userAlias) {
        List<String> followers  = new ArrayList<>();

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#sort", sortKey);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":followee_handle", new AttributeValue().withS(userAlias));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(table.getTableName())
                .withIndexName(index.getIndexName())
                .withKeyConditionExpression("#sort = :followee_handle")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues);
                //.withLimit(pageSize);

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items) {
                String follower = item.get("follower_handle").getS();
                followers.add(follower);
            }
        }
        return followers;
    }

    @Override
    public List<String> getFollowing(String userAlias) {
        List<String> following  = new ArrayList<>();

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#part", partKey);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follower_handle", new AttributeValue().withS(userAlias));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(table.getTableName())
                .withKeyConditionExpression("#part = :follower_handle")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues);
               // .withLimit(pageSize);

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items) {
                String followee = item.get("followee_handle").getS();
                following.add(followee);
            }
        }
        return following;
    }

    @Override
    public int getFollowersCount(String userAlias) { return getFollowers(userAlias).size(); }

    @Override
    public int getFollowingCount(String userAlias) {
        return getFollowing(userAlias).size();
    }

    @Override
    public void follow(String followerAlias, String followeeAlias) {
        Item item = new Item().withPrimaryKey(partKey, followerAlias, sortKey, followeeAlias);
        table.putItem(item);
    }

    @Override
    public void unfollow(String followerAlias, String followeeAlias) {
        table.deleteItem(partKey, followerAlias, sortKey, followeeAlias);
    }

}