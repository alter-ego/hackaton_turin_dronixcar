package solutions.alterego.dronix.droidcar.di;

import javax.inject.Singleton;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import solutions.alterego.dronix.droidcar.App;
import solutions.alterego.dronix.droidcar.MainActivity;

@Singleton
@dagger.Component(modules = {SystemServicesModule.class})
public interface Component {

    void inject(App app);

    void inject(MainActivity mainActivity);

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class Initializer {

        public static Component init(App app) {
            return DaggerComponent.builder()
                    .systemServicesModule(new SystemServicesModule(app))
                    .build();
        }
    }
}