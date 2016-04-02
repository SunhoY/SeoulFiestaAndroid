package io.harry.seoulfiesta;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public class BaseTest {
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestSeoulFiestaApplication.inject(this);
    }
}
