package io.harry.seoulfiesta.service;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.api.VacationApi;
import io.harry.seoulfiesta.model.Vacation;
import io.harry.seoulfiesta.model.VacationItem;
import io.harry.seoulfiesta.model.json.VacationItemResponse;
import io.harry.seoulfiesta.model.json.VacationRequest;
import io.harry.seoulfiesta.model.json.VacationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacationService {
    public static final String VACATION_TYPE_NORMAL = "normal";
    public static final String VACATION_TYPE_FAMILY = "family";
    public static final String VACATION_TYPE_OFFICIAL = "official";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    private final Context context;

    @Inject
    VacationApi vacationApi;

    public VacationService(Context context) {
        SeoulFiestaApplication.inject(this);
        this.context= context;
    }

    public void postVacation(Vacation vacation, final ServiceCallback<Void> serviceCallback) {
        VacationRequest vacationJson = getVacationJson(vacation);

        Call<Void> voidCall = vacationApi.postVacation(vacationJson);
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    serviceCallback.onSuccess(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void getVacations(final ServiceCallback<List<VacationItem>> serviceCallback) {
        Call<List<VacationResponse>> call = vacationApi.getVacations();
        call.enqueue(new Callback<List<VacationResponse>>() {
            @Override
            public void onResponse(Call<List<VacationResponse>> call, Response<List<VacationResponse>> response) {
                List<VacationResponse> vacationResponses = response.body();

                List<VacationItem> vacationItems = new ArrayList<>();
                for(VacationResponse vacationResponse : vacationResponses) {
                    for(VacationItemResponse vacationItemResponse: vacationResponse.vacationItems) {
                        vacationItems.add(new VacationItem(vacationResponse.vacationType, vacationResponse.vacationStatus, vacationItemResponse.vacationDate));
                    }
                }

                serviceCallback.onSuccess(vacationItems);
            }

            @Override
            public void onFailure(Call<List<VacationResponse>> call, Throwable t) {

            }
        });
    }

    private VacationRequest getVacationJson(Vacation vacation) {
        VacationRequest vacationJson = new VacationRequest();

        vacationJson.userId = vacation.getUserId();
        if(vacation.getType().equals(getString(R.string.vacation_type_normal))) {
            vacationJson.type = VACATION_TYPE_NORMAL;
        } else if(vacation.getType().equals(getString(R.string.vacation_type_family))) {
            vacationJson.type = VACATION_TYPE_FAMILY;
        } else {
            vacationJson.type = VACATION_TYPE_OFFICIAL;
        }

        vacationJson.startDate = vacation.getStart().toString(YYYY_MM_DD);
        vacationJson.endDate = vacation.getEnd().toString(YYYY_MM_DD);
        vacationJson.reason = vacation.getReason();
        return vacationJson;
    }

    @NonNull
    private String getString(int resourceId) {
        return context.getResources().getString(resourceId);
    }
}