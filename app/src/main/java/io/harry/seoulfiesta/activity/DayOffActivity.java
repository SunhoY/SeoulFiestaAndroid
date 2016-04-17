package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.dialog.DatePickerFragment;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

public class DayOffActivity extends BaseActivity {
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    UserService userService;

    @Bind(R.id.department)
    TextView department;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.rank)
    TextView rank;
    @Bind(R.id.days_off)
    TextView daysOff;
    @Bind(R.id.day_off_type)
    Spinner dayOffType;
    @Bind(R.id.days_off_start)
    TextView daysOffStart;
    @Bind(R.id.days_off_end)
    TextView daysOffEnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.day_off_activity);
        ButterKnife.bind(this);
        SeoulFiestaApplication.inject(this);
        super.onCreate(savedInstanceState);

        department.setText(sharedPreferences.getString("department", "지정되지 않음"));
        userName.setText(sharedPreferences.getString("userName", "홍길동"));
        rank.setText(sharedPreferences.getString("rank", "사원"));
        int id = sharedPreferences.getInt("id", -1);
        userService.getDaysOffPerYear(id, new ServiceCallback<Integer>() {
            @Override
            public void onSuccess(Integer daysOff) {
                DayOffActivity.this.daysOff.setText(String.valueOf(daysOff));
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        ArrayAdapter<CharSequence> dayOffTypesAdapter =
                ArrayAdapter.createFromResource(this, R.array.day_off_types, android.R.layout.simple_spinner_item);
        dayOffTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOffType.setAdapter(dayOffTypesAdapter);
    }

    @OnClick(R.id.days_off_start)
    public void onDaysOffStartClick(final TextView daysOffStart) {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setOnResultListener(new DatePickerFragment.OnResultListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                daysOffStart.setText(getString(R.string.date_string, year, month, day));
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "date picker dialog");
    }

    @OnClick(R.id.days_off_end)
    public void onDaysOffEndClick(final TextView daysOffEnd) {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setOnResultListener(new DatePickerFragment.OnResultListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                daysOffEnd.setText(getString(R.string.date_string, year, month, day));
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "date picker dialog");
    }
}
