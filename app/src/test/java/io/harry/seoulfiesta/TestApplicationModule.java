package io.harry.seoulfiesta;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.seoulfiesta.activity.LoginActivity;
import io.harry.seoulfiesta.activity.LoginActivityTest;
import io.harry.seoulfiesta.api.UserApi;
import io.harry.seoulfiesta.service.UserService;
import io.harry.seoulfiesta.service.UserServiceTest;

import static org.mockito.Mockito.mock;

@Module(injects = {
            LoginActivity.class,
            LoginActivityTest.class,
            UserService.class,
            UserServiceTest.class,
        }
)
public class TestApplicationModule {
    @Provides @Singleton UserService provideUserService() { return mock(UserService.class); }
    @Provides @Singleton UserApi providesUserApi() {return mock(UserApi.class);}
}
