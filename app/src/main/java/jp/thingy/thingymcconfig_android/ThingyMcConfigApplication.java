package jp.thingy.thingymcconfig_android;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import jp.thingy.thingymcconfig.ThingyMcConfig;
import jp.thingy.thingymcconfig_android.activity.MainActivity;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigProgressViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.ConfigurationViewModel;
import jp.thingy.thingymcconfig_android.viewmodel.NetworkSelectViewModel;

public class ThingyMcConfigApplication extends Application {

    private static AppComponent component;

    @Module
    public static class AppModule {

        private final ThingyMcConfig thingyMcConfig;

        public AppModule(ThingyMcConfig thingyMcConfig) {
            this.thingyMcConfig = thingyMcConfig;
        }

        @Provides
        @Singleton
        ThingyMcConfig thingyMcConfig() {
            return thingyMcConfig;
        }
    }

    @Singleton
    @Component(modules = {AppModule.class})
    public interface AppComponent {
        void inject(MainActivity activity);

        void inject(NetworkSelectViewModel viewModel);

        void inject(ConfigurationViewModel viewModel);

        void inject(ConfigProgressViewModel viewModel);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ThingyMcConfig thingyMcConfig = new ThingyMcConfig(this, "thingy");
        component = DaggerThingyMcConfigApplication_AppComponent.builder()
                .appModule(new AppModule(thingyMcConfig))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
