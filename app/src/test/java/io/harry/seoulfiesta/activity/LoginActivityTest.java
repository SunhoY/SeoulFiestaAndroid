package io.harry.seoulfiesta.activity;


import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.model.json.User;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginActivityTest {

    private LoginActivity subject;

    @Bind(R.id.user_name) EditText userName;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.login) TextView login;

    @Inject UserService userService;
    @Captor ArgumentCaptor<ServiceCallback<User>> userServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = Robolectric.setupActivity(LoginActivity.class);
        ButterKnife.bind(this, subject);
    }

    @Test
    public void clickingOnLoginButton_callsUserServiceWithUserNameAndPassword_whenFieldsAreFilled() throws Exception {
        userName.setText("harry");
        password.setText("1qaz");

        login.performClick();

        verify(userService).login(eq("harry"), eq("1qaz"), any(ServiceCallback.class));
    }

    @Test
    public void whenUserLoggedInSuccessfully_launchesMenuActivity() throws Exception {
        userName.setText("harry");
        password.setText("1qaz");

        login.performClick();

        verify(userService).login(eq("harry"), eq("1qaz"), userServiceCallbackCaptor.capture());
        User user = new User();
        user.userName = "harry";
        user.password = "1qaz";
        userServiceCallbackCaptor.getValue().onSuccess(user);

        Intent actualActivity = shadowOf(subject).getNextStartedActivity();
        Intent expectedActivity = new Intent(subject, MenuActivity.class);

        assertThat(actualActivity).isEqualTo(expectedActivity);
    }

    @Test
    public void whenUserCouldNotLogin_showsToast() throws Exception {
        userName.setText("harry");
        password.setText("1qaz");

        login.performClick();

        verify(userService).login(eq("harry"), eq("1qaz"), userServiceCallbackCaptor.capture());
        userServiceCallbackCaptor.getValue().onFailure("something went wrong");

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("something went wrong");
    }
}
