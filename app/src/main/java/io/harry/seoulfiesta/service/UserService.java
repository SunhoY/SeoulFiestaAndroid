package io.harry.seoulfiesta.service;

import javax.inject.Inject;

import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    @Inject UserApi userApi;

    public UserService() {
        SeoulFiestaApplication.inject(this);
    }

    public void login(String userName, String password, final ServiceCallback<User> serviceCallback) {
        Call<User> service = userApi.userLogin(new User(userName, password));
        service.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                serviceCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                serviceCallback.onFailure(t.getMessage());
            }
        });

    }
}
