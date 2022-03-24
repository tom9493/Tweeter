package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.Interface.StoryDAOInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

public class StoryDAO implements StoryDAOInterface {
    private final String partKey = "sender_alias";
    private final String sortKey = "time_stamp";

    protected static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-1").build();
    protected static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
    private final Table table = dynamoDB.getTable("Story");

    @Override
    public List<Status> getStory(User user, int pageSize, Status lastStatus) {
        List<Status> story = new ArrayList<>();

        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#partKey", partKey);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":sender_alias", new AttributeValue().withS(user.getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(table.getTableName())
                .withKeyConditionExpression("#partKey = :sender_alias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues);

//        if (lastStatus != null) {
//            Map<String, AttributeValue> startKey = new HashMap<>();
//            startKey.put(partKey, new AttributeValue().withS(user.getAlias()));
//            startKey.put("time_stamp", new AttributeValue().withS(String.valueOf(lastStatus.getTimeStamp())));
//
//            queryRequest = queryRequest.withExclusiveStartKey(startKey);
//        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String,AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String,AttributeValue> item : items) {
                Status status = new Status(item.get("post").getS(), user, parseLong(item.get("time_stamp").getN()),
                        item.get("urls").getSS(), item.get("mentions").getSS());
                story.add(status);
            }
        }
        return story;
    }

    @Override
    public void addStatusToStory(Status status) {
        Item item = new Item().withPrimaryKey(partKey, status.getUser().getAlias(), sortKey, System.currentTimeMillis())
                .withString("post", status.getPost())
                .withList("mentions", status.getMentions())
                .withList("urls", status.getUrls());
        table.putItem(item);
    }
}
