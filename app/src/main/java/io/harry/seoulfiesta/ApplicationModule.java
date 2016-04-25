package io.harry.seoulfiesta;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import io.harry.seoulfiesta.activity.VacationActivity;
import io.harry.seoulfiesta.activity.LoginActivity;
import io.harry.seoulfiesta.api.VacationApi;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.service.VacationService;
import io.harry.seoulfiesta.service.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(injects = {
        LoginActivity.class,
        UserService.class,
        VacationActivity.class,
        VacationService.class,
})
public class ApplicationModule {
    public static final String SHARED_PREFERENCES_NAME = "SeoulFiesta";
    private Context context;
    private Retrofit retrofit;

    ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides UserService provideUserService() { return new UserService(); }
    @Provides
    VacationService provideVacationService() {return new VacationService(context); }
    @Provides UserApi provideUserApi() { return getRetrofit().create(UserApi.class); }
    @Provides
    VacationApi provideVacationApi() {return getRetrofit().create(VacationApi.class); }
    @Provides
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    Retrofit getRetrofit() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.backend_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
