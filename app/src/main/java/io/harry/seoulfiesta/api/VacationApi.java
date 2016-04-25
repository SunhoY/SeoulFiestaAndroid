package io.harry.seoulfiesta.api;

import io.harry.seoulfiesta.model.json.VacationJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface VacationApi {
    @POST("/vacations")
    Call<Void> postVacation(@Body VacationJson vacationJson);
}
