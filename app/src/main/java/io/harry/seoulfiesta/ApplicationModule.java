package io.harry.seoulfiesta;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.harry.seoulfiesta.activity.LoginActivity;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.service.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(injects = {
    LoginActivity.class,
    UserService.class
})
public class ApplicationModule {
    private Context context;
    private Retrofit retrofit;

    ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides UserService provideUserService() { return new UserService(); }

    @Provides
    UserApi provideUserApi() { return getRetrofit().create(UserApi.class); }

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
