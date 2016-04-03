package io.harry.seoulfiesta.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginActivityTest {

    private LoginActivity subject;

    @Bind(R.id.email) EditText email;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.login) TextView login;

    @Inject UserService userService;
    @Inject SharedPreferences sharedPreferences;
    @Mock SharedPreferences.Editor editor;

    @Captor ArgumentCaptor<ServiceCallback<User>> userServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);
        when(editor.commit()).thenReturn(true);

        subject = Robolectric.setupActivity(LoginActivity.class);
        ButterKnife.bind(this, subject);
    }

    @Test
    public void clickingOnLoginButton_callsUserServiceWithUserNameAndPassword_whenFieldsAreFilled() throws Exception {
        login("harry@harry.io", "1qaz");

        verify(userService).login(eq("harry@harry.io"), eq("1qaz"), any(ServiceCallback.class));
    }

    @Test
    public void whenUserLoggedInSuccessfully_launchesMenuActivity() throws Exception {
        login("harry@harry.io", "1qaz");

        verify(userService).login(eq("harry@harry.io"), eq("1qaz"), userServiceCallbackCaptor.capture());
        User user = new User();
        user.email = "harry@harry.io";
        user.userName = "해리";
        user.department = "사업부";
        user.rank = "대리";

        userServiceCallbackCaptor.getValue().onSuccess(user);

        Intent actualActivity = shadowOf(subject).getNextStartedActivity();
        Intent expectedActivity = new Intent(subject, MenuActivity.class);

        assertThat(actualActivity).isEqualTo(expectedActivity);
    }

    @Test
    public void afterUserLoginSuccessfully_savesUserDataOnSharedPreferences() throws Exception {
        login("harry@harry.io", "1qaz");

        verify(userService).login(eq("harry@harry.io"), eq("1qaz"), userServiceCallbackCaptor.capture());
        User user = new User();
        user.id = 43;
        user.email = "harry@harry.io";
        user.userName = "해리";
        user.department = "사업부";
        user.rank = "대리";

        userServiceCallbackCaptor.getValue().onSuccess(user);

        verify(sharedPreferences).edit();
        verify(editor).putInt("id", 43);
        verify(editor).putString("email", "harry@harry.io");
        verify(editor).putString("userName", "해리");
        verify(editor).putString("department", "사업부");
        verify(editor).putString("rank", "대리");

        verify(editor).apply();
    }

    @Test
    public void whenUserCouldNotLogin_showsToast() throws Exception {
        login("harry", "1qaz");

        verify(userService).login(eq("harry"), eq("1qaz"), userServiceCallbackCaptor.capture());
        userServiceCallbackCaptor.getValue().onFailure("something went wrong");

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("something went wrong");
    }

    public void login(String email, String password) throws Exception {
        this.email.setText(email);
        this.password.setText(password);

        login.performClick();
    }
}
