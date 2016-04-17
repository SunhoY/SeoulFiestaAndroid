package io.harry.seoulfiesta.service;

import java.io.IOException;

import javax.inject.Inject;

import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.model.json.UserJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    @Inject UserApi userApi;

    public UserService() {
        SeoulFiestaApplication.inject(this);
    }

    public void login(String email, String password, final ServiceCallback<UserJson> serviceCallback) {
        UserJson userJson = new UserJson();
        userJson.email = email;
        userJson.password = password;
        Call<UserJson> service = userApi.userLogin(userJson);
        service.enqueue(new Callback<UserJson>() {
            @Override
            public void onResponse(Call<UserJson> call, Response<UserJson> response) {
                if (response.isSuccessful()) {
                    serviceCallback.onSuccess(response.body());
                } else {
                    try {
                        serviceCallback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserJson> call, Throwable t) {
                serviceCallback.onFailure(t.getMessage());
            }
        });

    }

    public void getDaysOffPerYear(int id, final ServiceCallback<Integer> serviceCallback) {
        Call<UserJson> service = userApi.getUser(id);
        service.enqueue(new Callback<UserJson>() {
            @Override
            public void onResponse(Call<UserJson> call, Response<UserJson> response) {
                serviceCallback.onSuccess(response.body().daysOff);
            }

            @Override
            public void onFailure(Call<UserJson> call, Throwable t) {

            }
        });
    }
}
