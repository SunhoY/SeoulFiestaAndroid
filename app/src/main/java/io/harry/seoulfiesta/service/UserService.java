package io.harry.seoulfiesta.service;

import javax.inject.Inject;

import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.model.json.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    @Inject UserApi userApi;

    public UserService() {
        SeoulFiestaApplication.inject(this);
    }

    public void login(String userName, String password, final ServiceCallback<User> serviceCallback) {
        User user = new User();
        user.userName = userName;
        user.password = password;
        Call<User> service = userApi.userLogin(user);
        service.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    serviceCallback.onSuccess(response.body());
                }
                else {
                    serviceCallback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                serviceCallback.onFailure(t.getMessage());
            }
        });

    }
}
