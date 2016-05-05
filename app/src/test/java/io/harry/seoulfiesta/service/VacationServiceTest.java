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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.TestSeoulFiestaApplication;
import io.harry.seoulfiesta.api.VacationApi;
import io.harry.seoulfiesta.model.Vacation;
import io.harry.seoulfiesta.model.VacationItem;
import io.harry.seoulfiesta.model.json.VacationItemResponse;
import io.harry.seoulfiesta.model.json.VacationRequest;
import io.harry.seoulfiesta.model.json.VacationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @Mock
    ServiceCallback<List<VacationItem>> mockVacationItemServiceCallback;
    @Mock
    Call<List<VacationResponse>> mockVacationCall;

    @Captor
    ArgumentCaptor<Callback<Void>> voidCallbackCaptor;
    @Captor
    ArgumentCaptor<Callback<List<VacationResponse>>> vacationResponsesCallbackCaptor;

    private Method getVacationJson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestSeoulFiestaApplication.inject(this);
        when(mockVacationApi.postVacation(any(VacationRequest.class))).thenReturn(mockVoidCall);
        when(mockVacationApi.getVacations()).thenReturn(mockVacationCall);

        getVacationJson = VacationService.class.getDeclaredMethod("getVacationJson", Vacation.class);
        getVacationJson.setAccessible(true);

        subject = new VacationService(RuntimeEnvironment.application);
    }

    @Test
    public void postVacation_callsPostVacationApi_withDaysOffJsonList() throws Exception {
        Vacation vacation = new Vacation(23, "일반 휴가", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        subject.postVacation(vacation, mockServiceCallback);

        VacationRequest vacationRequest = new VacationRequest();
        vacationRequest.type = "normal";
        vacationRequest.startDate = DATE_2016_6_19.toString("yyyy-MM-dd");
        vacationRequest.endDate = DATE_2016_6_22.toString("yyyy-MM-dd");
        vacationRequest.reason = "아파서\n쉽니다.";
        vacationRequest.userId = 23;

        verify(mockVacationApi).postVacation(vacationRequest);
        verify(mockVoidCall).enqueue(voidCallbackCaptor.capture());

        Response<Void> response = Response.success(null);

        voidCallbackCaptor.getValue().onResponse(mockVoidCall, response);

        verify(mockServiceCallback).onSuccess(null);
    }

    @Test
    public void getVacations_callsGetVacationApi_runsSuccessCallback_whenSuccess() throws Exception {
        subject.getVacations(mockVacationItemServiceCallback);

        verify(mockVacationApi).getVacations();
        verify(mockVacationCall).enqueue(vacationResponsesCallbackCaptor.capture());

        VacationResponse normal = getVacationResponsesWithItemCount("requested", "normal", 2);
        VacationResponse family = getVacationResponsesWithItemCount("requested", "family", 3);
        List<VacationResponse> vacationResponses = new ArrayList<>();
        vacationResponses.addAll(Arrays.asList(normal, family));

        Response<List<VacationResponse>> success = Response.success(vacationResponses);

        vacationResponsesCallbackCaptor.getValue().onResponse(mockVacationCall, success);

        List<VacationItem> vacationItems = new ArrayList<>();
        for(int i = 0; i < 2; ++i) {
            vacationItems.add(
                    new VacationItem(normal.vacationType,
                            normal.vacationStatus,
                            normal.vacationItems.get(i).vacationDate));
        }
        for(int i = 0; i < 3; ++i) {
            vacationItems.add(
                    new VacationItem(family.vacationType,
                            family.vacationStatus,
                            family.vacationItems.get(i).vacationDate));
        }

        verify(mockVacationItemServiceCallback).onSuccess(vacationItems);
    }

    @Test
    public void getVacationJson_createsJsonObject_dependsOnParameters() throws Exception {
        Vacation normal = new Vacation(23, "일반 휴가", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        VacationRequest vacationJson = (VacationRequest) getVacationJson.invoke(subject, normal);

        assertThat(vacationJson.userId).isEqualTo(23);
        assertThat(vacationJson.type).isEqualTo("normal");
        assertThat(vacationJson.startDate).isEqualTo(DATE_2016_6_19.toString("yyyy-MM-dd"));
        assertThat(vacationJson.endDate).isEqualTo(DATE_2016_6_22.toString("yyyy-MM-dd"));
        assertThat(vacationJson.reason).isEqualTo("아파서\n쉽니다.");

        Vacation family = new Vacation(23, "경조 휴가", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        vacationJson = (VacationRequest) getVacationJson.invoke(subject, family);

        assertThat(vacationJson.type).isEqualTo("family");

        Vacation official = new Vacation(23, "공가(예비군)", DATE_2016_6_19, DATE_2016_6_22, "아파서\n쉽니다.");
        vacationJson = (VacationRequest) getVacationJson.invoke(subject, official);

        assertThat(vacationJson.type).isEqualTo("official");
    }

    private VacationResponse getVacationResponsesWithItemCount(String status, String type, int count) {
        String baseDate = "2016-06-1";
        String time = "00:00:00";

        VacationResponse vacationResponse = new VacationResponse();
        vacationResponse.vacationType = type;
        vacationResponse.vacationStatus = status;
        vacationResponse.vacationItems = new ArrayList<>();

        for(int i = 0; i < count; ++i) {
            VacationItemResponse vacationItemResponse = new VacationItemResponse();
            vacationItemResponse.vacationDate = baseDate + i + " " + time;
            vacationResponse.vacationItems.add(vacationItemResponse);
        }
        return vacationResponse;
    }

}