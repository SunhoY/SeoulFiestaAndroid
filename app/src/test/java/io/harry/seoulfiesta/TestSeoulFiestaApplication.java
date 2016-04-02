package io.harry.seoulfiesta;

import android.app.Application;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import io.harry.seoulfiesta.wrapper.ObjectGraphWrapper;

public class TestSeoulFiestaApplication extends Application implements TestLifecycleApplication {

    static ObjectGraphWrapper objectGraphWrapper;

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectGraphWrapper.createObjectGraph(new TestApplicationModule());
    }

    public static void inject(Object object) {
        ObjectGraphWrapper.inject(object);
    }

    @Override
    public void beforeTest(Method method) {

    }

    @Override
    public void prepareTest(Object test) {
        TestSeoulFiestaApplication.inject(test);
    }

    @Override
    public void afterTest(Method method) {

    }
}
