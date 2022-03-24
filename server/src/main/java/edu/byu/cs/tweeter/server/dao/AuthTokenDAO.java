package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.Interface.AuthTokenDAOInterface;

public class AuthTokenDAO implements AuthTokenDAOInterface {
    protected static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-1").build();
    protected static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
    private final Table table = dynamoDB.getTable("AuthToken");
    private final String partitionKey = "token";
    private final String timeKey = "time_stamp";

    @Override
    public AuthToken findAuthToken(AuthToken authToken) throws Exception {
        Item item = table.getItem(partitionKey, authToken.getToken());
        authToken.setTimeStamp(System.currentTimeMillis());
        long time_stamp = item.getLong(timeKey);

        if (authToken.getTimeStamp() - time_stamp  > 14400000) {
            deleteAuthToken(authToken);
            throw new Exception("AuthToken expired. Log back in.");
        } else { insertAuthToken(authToken); }
        return authToken;
    }

    @Override
    public void insertAuthToken(AuthToken authToken) {
        Item item = new Item().withPrimaryKey(partitionKey, authToken.getToken())
                .withLong(timeKey, System.currentTimeMillis());
        table.putItem(item);
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) {
        table.deleteItem(partitionKey, authToken.getToken());
    }
}