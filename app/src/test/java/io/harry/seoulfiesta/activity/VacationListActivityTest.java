package io.harry.seoulfiesta.activity;

import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.R;
import io.harry.seoulfiesta.TestSeoulFiestaApplication;
import io.harry.seoulfiesta.model.VacationItem;
import io.harry.seoulfiesta.service.ServiceCallback;
import io.harry.seoulfiesta.service.VacationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class VacationListActivityTest {
    private VacationListActivity subject;
    @Inject
    VacationService mockVacationService;
    @Captor
    ArgumentCaptor<ServiceCallback<List<VacationItem>>> vacationListServiceCallbackCaptor;

    @Bind(R.id.vacation_list)
    RecyclerView vacationList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestSeoulFiestaApplication.inject(this);
        subject = Robolectric.setupActivity(VacationListActivity.class);
        ButterKnife.bind(this, subject);
    }

    @Test
    public void onActivityLaunch_callsVacationService_toGetVacationList() throws Exception {
        verify(mockVacationService).getVacations(Matchers.<ServiceCallback<List<VacationItem>>>any());
    }

    @Test
    public void onActivityLaunch_showsListView_withVacationList_whenVacationServiceSuccessful() throws Exception {
        verify(mockVacationService).getVacations(vacationListServiceCallbackCaptor.capture());

        List<VacationItem> mockVacationList = mock(List.class);
        when(mockVacationList.size()).thenReturn(5);
        vacationListServiceCallbackCaptor.getValue().onSuccess(mockVacationList);

        assertThat(vacationList.getAdapter()).isNotNull();
        assertThat(vacationList.hasFixedSize()).isTrue();
        assertThat(vacationList.getAdapter().getItemCount()).isEqualTo(5);
    }
}