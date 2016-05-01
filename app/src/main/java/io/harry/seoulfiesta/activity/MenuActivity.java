package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import io.harry.seoulfiesta.R;

public class MenuActivity extends BaseActivity {
    @Bind(R.id.vacation)
    TextView vacation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.menu_activity);
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.vacation)
    public void onVacationClick() {
        startActivity(new Intent(this, VacationMenuActivity.class));
    }
}
