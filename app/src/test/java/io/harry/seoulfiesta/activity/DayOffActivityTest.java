package io.harry.seoulfiesta.activity;

import android.content.SharedPreferences;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.harry.seoulfiesta.BuildConfig;
import io.harry.seoulfiesta.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DayOffActivityTest {
    DayOffActivity subject;

    @Inject SharedPreferences sharedPreferences;

    @Bind(R.id.department) TextView department;
    @Bind(R.id.user_name) TextView userName;
    @Bind(R.id.rank) TextView rank;

    @Before
    public void setUp() throws Exception {
        when(sharedPreferences.getString(eq("department"), anyString())).thenReturn("땡보사업부");
        when(sharedPreferences.getString(eq("userName"), anyString())).thenReturn("양해리");
        when(sharedPreferences.getString(eq("rank"), anyString())).thenReturn("대리");
        subject = Robolectric.setupActivity(DayOffActivity.class);
        ButterKnife.bind(this, subject);
    }

    @Test
    public void onActivityStart_showsDepartment() throws Exception {
        assertThat(department.getText()).isEqualTo("땡보사업부");
    }
}