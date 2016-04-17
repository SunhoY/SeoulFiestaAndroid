package io.harry.seoulfiesta.activity;

import android.content.pm.ActivityInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import io.harry.seoulfiesta.BuildConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BaseActivityTest {
    BaseActivity subject;

    @Test
    public void testScreenOrientationIsPortrait() throws Exception {
        subject = Robolectric.setupActivity(BaseActivity.class);
        assertThat(subject.getRequestedOrientation()).isEqualTo(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}