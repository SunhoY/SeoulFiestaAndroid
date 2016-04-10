package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

public class DayOffActivity extends BaseActivity {

    @Inject SharedPreferences sharedPreferences;
    @Inject UserService userService;

    @Bind(R.id.department) TextView department;
    @Bind(R.id.user_name) TextView userName;
    @Bind(R.id.rank) TextView rank;
    @Bind(R.id.days_off) TextView daysOff;

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
    }
}
