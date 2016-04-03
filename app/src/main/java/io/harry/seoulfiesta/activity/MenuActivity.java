package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import io.harry.seoulfiesta.R;

public class MenuActivity extends BaseActivity {

    @Bind(R.id.day_off) TextView dayOff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.menu_activity);
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.day_off)
    public void onDayOffClick() {
        startActivity(new Intent(this, DayOffActivity.class));
    }
}
