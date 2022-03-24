package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.Interface.AbstractFactory;
import edu.byu.cs.tweeter.server.Interface.AuthTokenDAOInterface;

public abstract class ValidationService {
    protected AbstractFactory factory;
    public ValidationService(AbstractFactory factory) { this.factory = factory; }

    public void validateAuthToken(AuthToken authToken) throws Exception {
        if (getAuthTokenDAO().findAuthToken(authToken) == null) {
            throw new RuntimeException("[BadRequest] Request needs valid authToken.");
        }
    }

    public AuthTokenDAOInterface getAuthTokenDAO() { return factory.getAuthTokenDAO(); }
}
