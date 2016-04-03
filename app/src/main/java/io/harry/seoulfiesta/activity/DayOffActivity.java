package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;

public class DayOffActivity extends BaseActivity {

    @Inject SharedPreferences sharedPreferences;

    @Bind(R.id.department) TextView department;
    @Bind(R.id.user_name) TextView userName;
    @Bind(R.id.rank) TextView rank;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.day_off_activity);
        ButterKnife.bind(this);
        SeoulFiestaApplication.inject(this);
        super.onCreate(savedInstanceState);

        department.setText(sharedPreferences.getString("department", "지정되지 않음"));
        userName.setText(sharedPreferences.getString("userName", "홍길동"));
        rank.setText(sharedPreferences.getString("rank", "사원"));
    }
}
