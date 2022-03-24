package edu.byu.cs.tweeter.server.Interface;

import com.amazonaws.services.dynamodbv2.document.Item;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAOInterface {
    public abstract AuthToken findAuthToken(AuthToken authToken) throws Exception;
    public abstract void insertAuthToken(AuthToken authToken) throws Exception;
    public abstract void deleteAuthToken(AuthToken authToken);
}
