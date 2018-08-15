package jp.thingy.thingymcconfig_android;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import jp.thingy.jpthingythingymcconfig_androidrx.RxThingyMcConfig;
import jp.thingy.thingymcconfig_android.activity.MainActivity;
import jp.thingy.thingymcconfig_android.viewmodel.RxThingyViewModel;

public class ThingyMcConfigApplication extends Application {

    private static AppComponent component;

    @Module
    public static class AppModule {

        private final RxThingyMcConfig thingyMcConfig;

        public AppModule(RxThingyMcConfig thingyMcConfig) {
            this.thingyMcConfig = thingyMcConfig;
        }

        @Provides
        @Singleton
        RxThingyMcConfig thingyMcConfig() {
            return thingyMcConfig;
        }
    }

    @Singleton
    @Component(modules = {AppModule.class})
    public interface AppComponent {
        void inject(MainActivity activity);

        void inject(RxThingyViewModel viewModel);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxThingyMcConfig thingyMcConfig = new RxThingyMcConfig(this, "thingy");
        component = DaggerThingyMcConfigApplication_AppComponent.builder()
                .appModule(new AppModule(thingyMcConfig))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
