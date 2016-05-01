package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.seoulfiesta.R;

public class VacationMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.vacation_menu_activity);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.request_vacations)
    public void onRequestVacationsClick() {
        startActivity(new Intent(this, VacationActivity.class));
    }

    @OnClick(R.id.vacation_list)
    public void onVactionListClick() {
        startActivity(new Intent(this, VacationListActivity.class));
    }
}