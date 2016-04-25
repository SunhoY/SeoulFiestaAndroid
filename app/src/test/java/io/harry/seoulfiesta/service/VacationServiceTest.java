package io.harry.seoulfiesta.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

import javax.inject.Inject;

import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.TestSeoulFiestaApplication;
import io.harry.seoulfiesta.api.VacationApi;
import io.harry.seoulfiesta.model.Vacation;
import io.harry.seoulfiesta.model.json.VacationJson;
import retrofit2.Call;
import retrofit2.Callback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class VacationServiceTest {
    private VacationService subject;

    final private DateTime DATE_2016_6_19 = new DateTime(2016, 6, 19, 0, 0);
    final private DateTime DATE_2016_6_22 = new DateTime(2016, 6, 22, 0, 0);

    @Inject
    VacationApi mockVacationApi;

    @Mock
    ServiceCallback<Void> mockServiceCallback;
    @Mock
    Call<Void> mockVoidCall;

    @Captor
    ArgumentCaptor<Callback<Void>> voidCallbackCaptor;
    private Method getVacationJson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestSeoulFiestaApplication.inject(this);
        when(mockVacationApi.postVacation(any(VacationJson.class))).thenReturn(mockVoidCall);

        getVacationJson = VacationService.class.getDeclaredMethod("getVacationJson", Vacation.class);
        getVacationJson.setAccessible(true);

        subject = new VacationService(RuntimeEnvironment.application);
    }

    @Test
    public void postVacation_callsPostDaysOffApi_withDaysOffJsonList() throws Exception {
        Vacation vacation = new Vacation(23, "일반 휴가", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        subject.postVacation(vacation, mockServiceCallback);

        VacationJson vacationJson = new VacationJson();
        vacationJson.type = "normal";
        vacationJson.startDate = DATE_2016_6_19;
        vacationJson.endDate = DATE_2016_6_22;
        vacationJson.reason = "아파서\n쉽니다.";
        vacationJson.userId = 23;

        verify(mockVacationApi).postVacation(vacationJson);
        verify(mockVoidCall).enqueue(voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onResponse(mockVoidCall, null);

        verify(mockServiceCallback).onSuccess(null);
    }

    @Test
    public void getVacationJson_createsJsonObject_dependsOnParameters() throws Exception {
        Vacation normal = new Vacation(23, "일반 휴가", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        VacationJson vacationJson = (VacationJson) getVacationJson.invoke(subject, normal);

        assertThat(vacationJson.userId).isEqualTo(23);
        assertThat(vacationJson.type).isEqualTo("normal");
        assertThat(vacationJson.startDate).isEqualTo(DATE_2016_6_19);
        assertThat(vacationJson.endDate).isEqualTo(DATE_2016_6_22);
        assertThat(vacationJson.reason).isEqualTo("아파서\n쉽니다.");

        Vacation family = new Vacation(23, "경조 휴가", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        vacationJson = (VacationJson) getVacationJson.invoke(subject, family);

        assertThat(vacationJson.type).isEqualTo("family");

        Vacation official = new Vacation(23, "공가(예비군)", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        vacationJson = (VacationJson) getVacationJson.invoke(subject, official);

        assertThat(vacationJson.type).isEqualTo("official");
    }
}