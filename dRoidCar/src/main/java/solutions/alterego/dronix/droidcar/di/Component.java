package solutions.alterego.dronix.droidcar.di;

import javax.inject.Singleton;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import solutions.alterego.dronix.droidcar.App;
import solutions.alterego.dronix.droidcar.FullScreenActivity;
import solutions.alterego.dronix.droidcar.MainActivity;
import solutions.alterego.dronix.droidcar.fragment.ArrowFragment;
import solutions.alterego.dronix.droidcar.fragment.SettingsFragment;

@Singleton
@dagger.Component(modules = {ManagersModule.class})
public interface Component {

    void inject(App app);

    void inject(MainActivity mainActivity);

    void inject(FullScreenActivity fullScreenActivity);

    void inject(ArrowFragment arrowFragment);

    void inject(SettingsFragment settingsFragment);

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class Initializer {

        public static Component init(App app) {
            return DaggerComponent.builder()
                    .managersModule(new ManagersModule(app))
                    .build();
        }
    }
}