package io.harry.seoulfiesta.adapter;

import android.widget.LinearLayout;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.model.VacationItem;

import static io.harry.seoulfiesta.adapter.VacationListAdapter.ViewHolder;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class VacationListAdapterTest {
    private VacationListAdapter subject;

    @Before
    public void setUp() throws Exception {
        subject = new VacationListAdapter();
    }

    @Test
    public void getItemCount_returnsNumberOfItemsInAdapter() throws Exception {
        List<VacationItem> vacationItems = generateVacations(5);
        subject.setData(vacationItems);

        assertThat(subject.getItemCount()).isEqualTo(5);

        vacationItems = generateVacations(3);
        subject.setData(vacationItems);

        assertThat(subject.getItemCount()).isEqualTo(3);
    }

    @Test
    public void onBindView_bindsCorrectData_intoView() throws Exception {
        List<VacationItem> vacationItems = generateVacations(3);
        subject.setData(vacationItems);

        LinearLayout linearLayout = new LinearLayout(RuntimeEnvironment.application);
        ViewHolder vacation = subject.onCreateViewHolder(linearLayout, 0);

        subject.onBindViewHolder(vacation, 0);

        assertThat(vacation.type.getText()).isEqualTo("일반 휴가");
        assertThat(vacation.status.getText()).isEqualTo("승인 요청중");
        assertThat(vacation.date.getText()).isEqualTo("2016-06-19");

        subject.onBindViewHolder(vacation, 1);

        assertThat(vacation.type.getText()).isEqualTo("경조 휴가");
        assertThat(vacation.status.getText()).isEqualTo("승인");
        assertThat(vacation.date.getText()).isEqualTo("2016-06-20");

        subject.onBindViewHolder(vacation, 2);

        assertThat(vacation.type.getText()).isEqualTo("공가(예비군)");
        assertThat(vacation.status.getText()).isEqualTo("반려");
        assertThat(vacation.date.getText()).isEqualTo("2016-06-21");
    }

    private List<VacationItem> generateVacations(int count) {
        String[] vacationTypes = new String[]{"일반 휴가", "경조 휴가", "공가(예비군)"};
        String[] vacationStatus = new String[]{"승인 요청중", "승인", "반려"};

        DateTime baseDate = new DateTime(2016, 6, 19, 8, 0);
        List<VacationItem> result = new ArrayList<>();
        for(int i = 0; i < count; ++i) {
            VacationItem vacationItem = new VacationItem(20, vacationStatus[i%3], baseDate.plusDays(i), vacationTypes[i%3]);
            result.add(vacationItem);
        }
        return result;
    }
}