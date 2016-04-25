package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.dialog.DatePickerFragment;
import io.harry.seoulfiesta.model.Vacation;
import io.harry.seoulfiesta.service.VacationService;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

public class VacationActivity extends BaseActivity {
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    UserService userService;
    @Inject
    VacationService vacationService;

    @Bind(R.id.department)
    TextView department;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.rank)
    TextView rank;
    @Bind(R.id.vacations_per_year)
    TextView vacationsPerYear;
    @Bind(R.id.vacation_type)
    Spinner vacationType;
    @Bind(R.id.vacation_start)
    TextView vacationStart;
    @Bind(R.id.vacation_end)
    TextView vacationEnd;
    @Bind(R.id.reason)
    EditText reason;

    DateTime startDate;
    DateTime endDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.vacation_activity);
        ButterKnife.bind(this);
        SeoulFiestaApplication.inject(this);
        super.onCreate(savedInstanceState);

        department.setText(sharedPreferences.getString("department", "지정되지 않음"));
        userName.setText(sharedPreferences.getString("userName", "홍길동"));
        rank.setText(sharedPreferences.getString("rank", "사원"));
        int id = sharedPreferences.getInt("id", -1);
        userService.getVacationsPerYear(id, new ServiceCallback<Integer>() {
            @Override
            public void onSuccess(Integer vacationPerYear) {
                VacationActivity.this.vacationsPerYear.setText(String.valueOf(vacationPerYear));
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        ArrayAdapter<CharSequence> vacationTypeAdapter =
                ArrayAdapter.createFromResource(this, R.array.vacation_types, android.R.layout.simple_spinner_item);
        vacationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vacationType.setAdapter(vacationTypeAdapter);
    }

    @OnClick(R.id.vacation_start)
    public void onVacationStartClick(final TextView vacationStart) {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setOnResultListener(new DatePickerFragment.OnResultListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                vacationStart.setText(getString(R.string.date_string, year, month, day));
                startDate = new DateTime(year, month, day, 0, 0);
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "date picker dialog");
    }

    @OnClick(R.id.vacation_end)
    public void onVacationEndClick(final TextView vacationEnd) {
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setOnResultListener(new DatePickerFragment.OnResultListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                vacationEnd.setText(getString(R.string.date_string, year, month, day));
                endDate = new DateTime(year, month, day, 0, 0);
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "date picker dialog");
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        int userId = sharedPreferences.getInt("id", -1);
        String type = vacationType.getSelectedItem().toString();
        String reason = this.reason.getText().toString();

        Vacation vacation = new Vacation(userId, type, startDate, endDate, reason);

        vacationService.postVacation(vacation, new ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void object) {
                Toast.makeText(VacationActivity.this, "휴가 승인 신청 완료", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
