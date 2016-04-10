package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DayOffActivityTest {
    DayOffActivity subject;

    @Inject SharedPreferences sharedPreferences;
    @Inject UserService userService;

    @Bind(R.id.department) TextView department;
    @Bind(R.id.user_name) TextView userName;
    @Bind(R.id.days_off) TextView daysOff;
    @Bind(R.id.rank) TextView rank;

    @Captor ArgumentCaptor<ServiceCallback<Integer>> integerServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(sharedPreferences.getString(eq("department"), anyString())).thenReturn("땡보사업부");
        when(sharedPreferences.getString(eq("userName"), anyString())).thenReturn("양해리");
        when(sharedPreferences.getString(eq("rank"), anyString())).thenReturn("대리");
        when(sharedPreferences.getInt(eq("id"), anyInt())).thenReturn(21);
        subject = Robolectric.setupActivity(DayOffActivity.class);
        ButterKnife.bind(this, subject);
    }

    @Test
    public void onActivityStart_showsDepartment() throws Exception {
        assertThat(department.getText()).isEqualTo("땡보사업부");
    }

    @Test
    public void onActivityStart_showsRank() throws Exception {
        assertThat(rank.getText()).isEqualTo("대리");
    }

    @Test
    public void onActivityStart_showsUserName() throws Exception {
        assertThat(userName.getText()).isEqualTo("양해리");
    }

    @Test
    public void onActivityStart_callsUserServiceToGetDaysOff() throws Exception {
        verify(userService).getDaysOffPerYear(eq(21), any(ServiceCallback.class));
    }

    @Test
    public void afterGettingDaysOff_setDaysOffOnDaysOffTextView() throws Exception {
        verify(userService).getDaysOffPerYear(eq(21), integerServiceCallbackCaptor.capture());

        integerServiceCallbackCaptor.getValue().onSuccess(12);

        assertThat(daysOff.getText()).isEqualTo("12");
    }
}