package io.harry.seoulfiesta.service;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.api.VacationApi;
import io.harry.seoulfiesta.model.Vacation;
import io.harry.seoulfiesta.model.json.VacationJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacationService {
    public static final String VACATION_TYPE_NORMAL = "normal";
    public static final String VACATION_TYPE_FAMILY = "family";
    public static final String VACATION_TYPE_OFFICIAL = "official";

    private final Context context;

    @Inject
    VacationApi vacationApi;

    public VacationService(Context context) {
        SeoulFiestaApplication.inject(this);
        this.context= context;
    }

    public void postVacation(Vacation vacation, final ServiceCallback<Void> serviceCallback) {
        VacationJson vacationJson = getVacationJson(vacation);

        Call<Void> voidCall = vacationApi.postVacation(vacationJson);
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                serviceCallback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private VacationJson getVacationJson(Vacation vacation) {
        VacationJson vacationJson = new VacationJson();

        vacationJson.userId = vacation.getUserId();
        if(vacation.getType().equals(getString(R.string.vacation_type_normal))) {
            vacationJson.type = VACATION_TYPE_NORMAL;
        } else if(vacation.getType().equals(getString(R.string.vacation_type_family))) {
            vacationJson.type = VACATION_TYPE_FAMILY;
        } else {
            vacationJson.type = VACATION_TYPE_OFFICIAL;
        }

        vacationJson.startDate = vacation.getStart();
        vacationJson.endDate = vacation.getEnd();
        vacationJson.reason = vacation.getReason();
        return vacationJson;
    }

    @NonNull
    private String getString(int resourceId) {
        return context.getResources().getString(resourceId);
    }
}