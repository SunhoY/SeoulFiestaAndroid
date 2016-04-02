package io.harry.seoulfiesta;

import android.app.Application;

import io.harry.seoulfiesta.wrapper.ObjectGraphWrapper;

public class SeoulFiestaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectGraphWrapper.createObjectGraph(new ApplicationModule(this));
    }

    public static void inject(Object object) {
        ObjectGraphWrapper.inject(object);
    }
}
