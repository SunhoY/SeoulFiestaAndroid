package io.harry.seoulfiesta.api;

import java.util.List;

import io.harry.seoulfiesta.model.json.VacationRequest;
import io.harry.seoulfiesta.model.json.VacationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface VacationApi {
    @POST("/vacations")
    Call<Void> postVacation(@Body VacationRequest vacationJson);

    @GET("/vacations")
    Call<List<VacationResponse>> getVacations();
}
