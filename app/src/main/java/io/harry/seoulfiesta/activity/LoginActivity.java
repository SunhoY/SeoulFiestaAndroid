package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.model.json.User;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

public class LoginActivity extends BaseActivity {

    @Inject UserService userService;
    @Bind(R.id.user_name) EditText userName;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.secret_login) TextView secretLogin;

    @OnClick(R.id.login)
    public void onLoginClick() {
        userService.login(userName.getText().toString(), password.getText().toString(), new ServiceCallback<User>() {
            @Override
            public void onSuccess(User object) {
                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    @OnLongClick(R.id.secret_login)
    public boolean onSecretLogin() {
        userName.setText("harry");
        password.setText("harry.io");
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_activity);
        SeoulFiestaApplication.inject(this);
        super.onCreate(savedInstanceState);
    }
}
