package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class VacationMenuActivityTest {
    VacationMenuActivity subject;

    @Bind(R.id.request_vacations)
    Button requestVacations;
    @Bind(R.id.vacation_list)
    Button vacationList;

    @Before
    public void setUp() throws Exception {
        subject = Robolectric.setupActivity(VacationMenuActivity.class);
        ButterKnife.bind(this, subject);
    }

    @Test
    public void clickingOnRequestVacationsButton_launchesVacationActivity() throws Exception {
        requestVacations.performClick();

        Intent expectedIntent = new Intent(subject, VacationActivity.class);
        Intent actualIntent = shadowOf(subject).getNextStartedActivity();
        assertThat(actualIntent).isEqualTo(expectedIntent);
    }

    @Test
    public void clickingOnVacationListButton_launchesVacationListActivity() throws Exception {
        vacationList.performClick();

        Intent expectedIntent = new Intent(subject, VacationListActivity.class);
        Intent actualIntent = shadowOf(subject).getNextStartedActivity();
        assertThat(actualIntent).isEqualTo(expectedIntent);
    }
}