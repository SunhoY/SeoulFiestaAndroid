package io.harry.seoulfiesta.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.harry.seoulfiesta.BaseTest;
import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.TestSeoulFiestaApplication;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.model.json.UserJson;
import io.harry.seoulfiesta.testutil.RetrofitTestUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UserServiceTest extends BaseTest {
    private UserService subject;
    @Inject UserApi userApi;
    @Captor ArgumentCaptor<Callback<UserJson>> userCallbackCaptor;
    @Mock Call<UserJson> mockCall;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestSeoulFiestaApplication.inject(this);
        when(userApi.userLogin(any(UserJson.class))).thenReturn(mockCall);
        when(userApi.getUser(anyInt())).thenReturn(mockCall);
        subject = new UserService();
    }

    @Test
    public void login_callsLoginApiOnUserApi_runsSuccessCallback_onSuccess() throws Exception {
        ServiceCallback<UserJson> mockServiceCallback = mock(ServiceCallback.class);
        UserJson userJson = new UserJson();
        userJson.email = "harry@harry.io";
        userJson.password = "secure";

        subject.login("harry@harry.io", "secure", mockServiceCallback);

        verify(userApi).userLogin(userJson);
        verify(mockCall).enqueue(userCallbackCaptor.capture());
        UserJson responseBody = new UserJson();
        userJson.id = 23;
        userJson.userName = "양해리";
        userJson.email = "harry@harry.io";
        userJson.rank = "대리";
        userJson.department = "연구소";

        Response<UserJson> response = Response.success(responseBody);

        userCallbackCaptor.getValue().onResponse(mockCall, response);

        verify(mockServiceCallback).onSuccess(responseBody);
    }

    @Test
    public void login_callsLoginApiOnUserApi_runsFailureCallback_onFailure() throws Exception {
        ServiceCallback mockServiceCallback = mock(ServiceCallback.class);
        UserJson userJson = new UserJson();
        userJson.email = "harry@harry.io";
        userJson.password = "secure";

        subject.login("harry@harry.io", "secure", mockServiceCallback);

        verify(userApi).userLogin(userJson);
        verify(mockCall).enqueue(userCallbackCaptor.capture());

        Response<UserJson> errorResponse = RetrofitTestUtil.createErrorResponse("text/plain", 404, "사용자가 존재하지 않습니다");
        userCallbackCaptor.getValue().onResponse(mockCall, errorResponse);

        verify(mockServiceCallback).onFailure("사용자가 존재하지 않습니다");
    }

    @Test
    public void getVacationsPerYear_callsUserApi_runsSuccessCallback_onSuccess() throws Exception {
        ServiceCallback mockServiceCallback = mock(ServiceCallback.class);
        subject.getVacationsPerYear(12, mockServiceCallback);

        verify(userApi).getUser(12);
        verify(mockCall).enqueue(userCallbackCaptor.capture());

        UserJson responseBody = new UserJson();
        responseBody.id = 23;
        responseBody.userName = "양해리";
        responseBody.email = "harry@harry.io";
        responseBody.rank = "대리";
        responseBody.department = "연구소";
        responseBody.vacationsPerYear = 15;

        Response<UserJson> response = Response.success(responseBody);
        userCallbackCaptor.getValue().onResponse(mockCall, response);

        verify(mockServiceCallback).onSuccess(15);
    }
}