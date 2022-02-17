package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;

public class MainPresenterUnitTest {
    private MainPresenter.View mockView;
    private StatusService mockStatusService;
    private LoginService mockLoginService;
    private Cache mockCache;

    private MainPresenter mainPresenterSpy;

    @Before
    public void setup() {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockLoginService = Mockito.mock(LoginService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
     //   Mockito.doReturn(mockLoginService).when(mainPresenterSpy).getLoginService(); // Use this when function returns void
        Mockito.when(mainPresenterSpy.getLoginService()).thenReturn(mockLoginService); // Use this for type checking
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    @Test
    public void testLogout_logoutSuccessful() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LogoutObserver observer = invocation.getArgument(1, MainPresenter.LogoutObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any(), Mockito.any());
        mainPresenterSpy.logout();

        Mockito.verify(mockView).logout();
    }

    @Test
    public void testLogout_logoutFailedWithMessage() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LogoutObserver observer = invocation.getArgument(1, MainPresenter.LogoutObserver.class);
                observer.handleFailure("Error message");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any(), Mockito.any());
        mainPresenterSpy.logout();

        Mockito.verify(mockView).displayErrorMessage("Failed to logout: Error message");
    }

    @Test
    public void testLogout_logoutFailedWithException() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LogoutObserver observer = invocation.getArgument(1, MainPresenter.LogoutObserver.class);
                Exception ex = new Exception("Error message");
                observer.handleException(ex);
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any(), Mockito.any());
        mainPresenterSpy.logout();

        Mockito.verify(mockView).displayErrorMessage("Failed to logout because of exception: Error message");
    }

    @Test
    public void testPostStatus_Successful() throws Exception {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.PostStatusObserver observer = invocation.getArgument(3, MainPresenter.PostStatusObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
        mainPresenterSpy.postStatus("This is a status post!");

        Mockito.verify(mockStatusService).postStatus(eq("This is a status post!"), Mockito.any(), Mockito.any(), isNotNull());
        Mockito.verify(mockView).postSuccess("Successfully posted!");
    }

    @Test
    public void testPostStatus_FailedWithMessage() throws Exception {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.PostStatusObserver observer = invocation.getArgument(3, MainPresenter.PostStatusObserver.class);
                observer.handleFailure("Error message");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
        mainPresenterSpy.postStatus("This is a status post!");

        Mockito.verify(mockStatusService).postStatus(eq("This is a status post!"), Mockito.any(), Mockito.any(), isNotNull());
        Mockito.verify(mockView).displayErrorMessage("Failed to post status: Error message");
    }

    @Test
    public void testPostStatus_FailedWithException() throws Exception {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.PostStatusObserver observer = invocation.getArgument(3, MainPresenter.PostStatusObserver.class);
                observer.handleException(new Exception("Error message"));
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
        mainPresenterSpy.postStatus("This is a status post!");

        Mockito.verify(mockStatusService).postStatus(eq("This is a status post!"), Mockito.any(), Mockito.any(), isNotNull());
        Mockito.verify(mockView).displayErrorMessage("Failed to post status because of exception: Error message");
    }
}
