package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class LogRegPresenter {
    private LogRegPresenter.View view;
    private LoginService loginService;

    public LogRegPresenter(View view) {
        this.view = view;
        loginService = new LoginService();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public interface View extends ViewInterface {
        void login(User registeredUser, String message);
    }

    public void setCacheValues(User loggedInUser, AuthToken authToken) {
        Cache.getInstance().setCurrUser(loggedInUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);
    }
}
