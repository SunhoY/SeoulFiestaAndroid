package io.harry.seoulfiesta.api;

import io.harry.seoulfiesta.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("user/login")
    Call<User> userLogin(@Body User user);
}
