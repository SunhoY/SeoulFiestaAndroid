package io.harry.seoulfiesta.service;

import android.app.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.harry.seoulfiesta.BaseTest;
import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UserServiceTest extends BaseTest {
    private UserService subject;
    @Inject UserApi userApi;
    @Captor ArgumentCaptor<Callback<User>> userCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new UserService();
    }

    @Test
    public void login_callsLoginApiOnUserApi_runsSuccessCallback_onSuccess() throws Exception {
        ServiceCallback mockServiceCallback = mock(ServiceCallback.class);
        User user = new User("harry", "secure");
        Call<User> mockCall = mock(Call.class);
        when(userApi.userLogin(user)).thenReturn(mockCall);
        subject.login("harry", "secure", mockServiceCallback);

        verify(userApi).userLogin(user);
        verify(mockCall).enqueue(userCallbackCaptor.capture());
        User responseBody = new User("harry", "@!#");
        Response<User> response = Response.success(responseBody);
        userCallbackCaptor.getValue().onResponse(mockCall, response);

        verify(mockServiceCallback).onSuccess(responseBody);
    }
}