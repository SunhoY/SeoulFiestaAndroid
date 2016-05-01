package io.harry.seoulfiesta.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.SeoulFiestaApplication;
import io.harry.seoulfiesta.adapter.VacationListAdapter;
import io.harry.seoulfiesta.model.VacationItem;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.VacationService;

public class VacationListActivity extends AppCompatActivity {
    @Inject
    VacationService vacationService;

    @Bind(R.id.vacation_list)
    RecyclerView vacationList;

    private VacationListAdapter vacationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SeoulFiestaApplication.inject(this);
        setContentView(R.layout.vacation_list_activity);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        vacationList.setHasFixedSize(true);
        vacationList.setLayoutManager(new LinearLayoutManager(this));
        vacationService.getVacations(new ServiceCallback<List<VacationItem>>() {
            @Override
            public void onSuccess(List<VacationItem> vacationItems) {
                vacationListAdapter = new VacationListAdapter();
                vacationListAdapter.setData(vacationItems);
                vacationList.setAdapter(vacationListAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}