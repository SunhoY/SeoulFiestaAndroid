package io.harry.seoulfiesta.activity;

import android.content.Intent;
import android.widget.TextView;

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
public class MenuActivityTest {
    MenuActivity subject;

    @Bind(R.id.days_off) TextView dayOff;

    @Test
    public void onRequestDayOffClick_launchesDayOffRequestActivity() throws Exception {
        subject = Robolectric.setupActivity(MenuActivity.class);
        ButterKnife.bind(this, subject);

        dayOff.performClick();

        Intent expected = new Intent(subject, DayOffActivity.class);
        Intent actual = shadowOf(subject).getNextStartedActivity();

        assertThat(actual).isEqualTo(expected);
    }
}