package io.harry.seoulfiesta.dialog;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DatePickerFragmentTest {
    private DatePickerFragment subject;

    @Test
    public void onDateSet_runsOnResultListenerCallback() throws Exception {
        subject = new DatePickerFragment();
        DatePickerFragment.OnResultListener onResultListener = mock(DatePickerFragment.OnResultListener.class);
        subject.setOnResultListener(onResultListener);

        subject.onDateSet(null, 2016, 10, 4);
        verify(onResultListener).onDateSet(2016, 11, 4);
    }
}