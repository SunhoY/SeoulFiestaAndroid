package io.harry.seoulfiesta.api;

import io.harry.seoulfiesta.model.json.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("/login")
    Call<UserJson> userLogin(@Body UserJson userJson);

    @GET("/users/{id}")
    Call<UserJson> getUser(@Path("id") int id);
}
