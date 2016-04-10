package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.model.json.UserJson;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

public class LoginActivity extends BaseActivity {

    @Inject UserService userService;
    @Inject SharedPreferences sharedPreferences;

    @Bind(R.id.email) EditText email;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.secret_login) TextView secretLogin;

    @OnClick(R.id.login)
    public void onLoginClick() {
        userService.login(email.getText().toString(), password.getText().toString(), new ServiceCallback<UserJson>() {
            @Override
            public void onSuccess(UserJson userJson) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", userJson.email);
                editor.putString("rank", userJson.rank);
                editor.putString("department", userJson.department);
                editor.putString("userName", userJson.userName);
                editor.putInt("id", userJson.id);
                editor.apply();

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
        email.setText("harry@harry.io");
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
