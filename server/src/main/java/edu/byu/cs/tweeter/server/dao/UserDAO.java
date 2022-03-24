package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.Interface.UserDAOInterface;

public class UserDAO implements UserDAOInterface {
    protected static final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-1").build();
    protected static final DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
    private final Table table = dynamoDB.getTable("User");
    private final String partitionKey = "user_alias";
    private final String passwordKey = "password";
    private final String firstKey = "firstname";
    private final String lastKey = "lastname";
    private final String imageKey = "imageURL";

    @Override
    public User addUser(String username, String password, String firstName, String lastName, String imageURL) {
        if (getUser(username) != null) { return null; }
        Item item = new Item().withPrimaryKey(partitionKey, username)
                .withString(passwordKey, password)
                .withString(firstKey, firstName)
                .withString(lastKey, lastName)
                .withString(imageKey, imageURL);
        table.putItem(item);
        return new User(firstName, lastName, username, imageURL);
    }

    @Override
    public User getUser(String userAlias) {
        Item item = table.getItem(partitionKey, userAlias);
        if (item != null) {
            return new User(item.getString(firstKey), item.getString(lastKey), item.getString(partitionKey),
                    item.getString(imageKey));
        }
        return null;
    }

    @Override
    public User getUser(String username, String password) {
        Item item = table.getItem(partitionKey, username);
        if (item.getString(passwordKey).equals(password)) {
            return new User(item.getString(firstKey), item.getString(lastKey), item.getString(partitionKey), item.getString(imageKey));
        }
        return null;
    }
}
