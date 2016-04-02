package io.harry.seoulfiesta.wrapper;

import dagger.ObjectGraph;

public class ObjectGraphWrapper {
    private static ObjectGraph objectGraph;

    public static void createObjectGraph(Object object) {
        objectGraph = ObjectGraph.create(object);
    }

    public static void inject(Object object) {
        objectGraph.inject(object);
    }
}
