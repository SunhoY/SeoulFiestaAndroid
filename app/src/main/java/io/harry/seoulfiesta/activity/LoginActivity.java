package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.model.User;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.UserService;

public class LoginActivity extends BaseActivity {

    @Inject UserService userService;
    @Bind(R.id.user_name) EditText userName;
    @Bind(R.id.password) EditText password;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_activity);
        super.onCreate(savedInstanceState);
    }
}
