package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
import android.widget.Spinner;
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
import io.harry.seoulfiesta.TestSeoulFiestaApplication;
import io.harry.seoulfiesta.dialog.DatePickerFragment;
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

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    UserService userService;

    @Bind(R.id.department)
    TextView department;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.days_off)
    TextView daysOff;
    @Bind(R.id.rank)
    TextView rank;
    @Bind(R.id.day_off_type)
    Spinner dayOffType;
    @Bind(R.id.days_off_start)
    TextView daysOffStart;
    @Bind(R.id.days_off_end)
    TextView daysOffEnd;

    @Captor
    ArgumentCaptor<ServiceCallback<Integer>> integerServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestSeoulFiestaApplication.inject(this);
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
    public void afterGettingDaysOff_setsDaysOffOnDaysOffTextView() throws Exception {
        verify(userService).getDaysOffPerYear(eq(21), integerServiceCallbackCaptor.capture());

        integerServiceCallbackCaptor.getValue().onSuccess(12);

        assertThat(daysOff.getText()).isEqualTo("12");
    }

    @Test
    public void dayOffTypeSpinner_hasThreeItems() throws Exception {
        assertThat(dayOffType.getAdapter().getCount()).isEqualTo(3);
    }

    @Test
    public void dayOffTypeSpinner_hasCorrectItems() throws Exception {
        assertThat(dayOffType.getAdapter().getItem(0)).isEqualTo("일반 휴가");
        assertThat(dayOffType.getAdapter().getItem(1)).isEqualTo("공가(예비군)");
        assertThat(dayOffType.getAdapter().getItem(2)).isEqualTo("경조 휴가");
    }

    @Test
    public void clickingOnDaysOffStart_showsDatePickerDialog() throws Exception {
        daysOffStart.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        assertThat(datePickerDialog.getDialog().isShowing()).isTrue();
    }

    @Test
    public void daysOffStartOnDateSet_setsDate() throws Exception {
        daysOffStart.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        datePickerDialog.onDateSet(null, 2016, 10, 4);

        assertThat(daysOffStart.getText().toString()).isEqualTo("2016-11-4");
    }

    @Test
    public void daysOffEndOnDateSet_setsDate() throws Exception {
        daysOffEnd.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        datePickerDialog.onDateSet(null, 2016, 10, 5);

        assertThat(daysOffEnd.getText().toString()).isEqualTo("2016-11-5");
    }
}