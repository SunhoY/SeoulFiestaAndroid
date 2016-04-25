package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;
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
import io.harry.seoulfiesta.TestSeoulFiestaApplication;
import io.harry.seoulfiesta.dialog.DatePickerFragment;
import io.harry.seoulfiesta.model.Vacation;
import io.harry.seoulfiesta.service.VacationService;
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
public class VacationActivityTest {
    VacationActivity subject;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    UserService userService;

    @Bind(R.id.department)
    TextView department;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.vacations_per_year)
    TextView vacationsPerYear;
    @Bind(R.id.rank)
    TextView rank;
    @Bind(R.id.vacation_type)
    Spinner vacationType;
    @Bind(R.id.vacation_start)
    TextView vacationStart;
    @Bind(R.id.vacation_end)
    TextView vacationEnd;
    @Bind(R.id.reason)
    EditText reason;
    @Bind(R.id.save)
    Button save;

    @Captor
    ArgumentCaptor<ServiceCallback<Integer>> integerServiceCallbackCaptor;
    @Captor
    ArgumentCaptor<ServiceCallback<Void>> voidServiceCallbackCaptor;
    @Inject
    VacationService mockVacationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestSeoulFiestaApplication.inject(this);
        when(sharedPreferences.getString(eq("department"), anyString())).thenReturn("땡보사업부");
        when(sharedPreferences.getString(eq("userName"), anyString())).thenReturn("양해리");
        when(sharedPreferences.getString(eq("rank"), anyString())).thenReturn("대리");
        when(sharedPreferences.getInt(eq("id"), anyInt())).thenReturn(21);
        subject = Robolectric.setupActivity(VacationActivity.class);
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
    public void onActivityStart_callsUserServiceToGetVacationsPerYear() throws Exception {
        verify(userService).getVacationsPerYear(eq(21), any(ServiceCallback.class));
    }

    @Test
    public void afterGettingVacationsPerYear_setsNumberOnRemainingVacationTextView() throws Exception {
        verify(userService).getVacationsPerYear(eq(21), integerServiceCallbackCaptor.capture());

        integerServiceCallbackCaptor.getValue().onSuccess(12);

        assertThat(vacationsPerYear.getText()).isEqualTo("12");
    }

    @Test
    public void vacationTypeSpinner_hasThreeItems() throws Exception {
        assertThat(vacationType.getAdapter().getCount()).isEqualTo(3);
    }

    @Test
    public void vacationTypeSpinner_hasCorrectItems() throws Exception {
        assertThat(vacationType.getAdapter().getItem(0)).isEqualTo("일반 휴가");
        assertThat(vacationType.getAdapter().getItem(1)).isEqualTo("공가(예비군)");
        assertThat(vacationType.getAdapter().getItem(2)).isEqualTo("경조 휴가");
    }

    @Test
    public void clickingOnVacationStart_showsDatePickerDialog() throws Exception {
        vacationStart.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        assertThat(datePickerDialog.getDialog().isShowing()).isTrue();
    }

    @Test
    public void vacationStartOnDateSet_setsDateOnVacationStartTextView() throws Exception {
        vacationStart.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        datePickerDialog.onDateSet(null, 2016, 10, 4);

        assertThat(vacationStart.getText().toString()).isEqualTo("2016-11-4");
    }

    @Test
    public void vacationEndOnDateSet_setsDateOnVacationEndTextView() throws Exception {
        vacationEnd.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        datePickerDialog.onDateSet(null, 2016, 10, 5);

        assertThat(vacationEnd.getText().toString()).isEqualTo("2016-11-5");
    }

    @Test
    public void clickingOnSave_callsDayOffService() throws Exception {
        vacationType.setSelection(0);

        setVacationStartDate(2016, 10, 4);
        setVacationEndDate(2016, 10, 6);

        reason.setText("몸이 아파서\n3일 쉽니다.");

        save.performClick();

        Vacation vacation = new Vacation(21, "일반 휴가", new DateTime(2016, 11, 4, 0, 0), new DateTime(2016, 11, 6, 0, 0), "몸이 아파서\n3일 쉽니다.");
        verify(mockVacationService).postVacation(eq(vacation), any(ServiceCallback.class));
    }

    @Test
    public void afterCallingVacationService_finishesActivity_onSuccess() throws Exception {
        vacationType.setSelection(0);

        setVacationStartDate(2016, 10, 4);
        setVacationEndDate(2016, 10, 6);

        reason.setText("몸이 아파서\n3일 쉽니다.");

        save.performClick();

        Vacation vacation = new Vacation(21, "일반 휴가", new DateTime(2016, 11, 4, 0, 0), new DateTime(2016, 11, 6, 0, 0), "몸이 아파서\n3일 쉽니다.");
        verify(mockVacationService).postVacation(eq(vacation),
                voidServiceCallbackCaptor.capture());

        ServiceCallback<Void> serviceCallback = voidServiceCallbackCaptor.getValue();
        serviceCallback.onSuccess(null);

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("휴가 승인 신청 완료");
        assertThat(subject.isFinishing()).isTrue();
    }

    private void setVacationEndDate(int year, int monthOfYear, int dayOfMonth) {
        vacationEnd.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        datePickerDialog.onDateSet(null, year, monthOfYear, dayOfMonth);
        datePickerDialog.dismiss();
    }

    private void setVacationStartDate(int year, int monthOfYear, int dayOfMonth) {
        vacationStart.performClick();

        DatePickerFragment datePickerDialog = (DatePickerFragment) subject.getSupportFragmentManager().findFragmentByTag("date picker dialog");
        datePickerDialog.onDateSet(null, year, monthOfYear, dayOfMonth);
        datePickerDialog.dismiss();
    }
}