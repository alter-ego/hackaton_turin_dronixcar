package solutions.alterego.dronix.droidcar.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import solutions.alterego.dronix.droidcar.api.CommandManager;
import solutions.alterego.dronix.droidcar.api.MotionManager;

@Module
public class ManagersModule {

    private final Application application;

    public ManagersModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providePreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    CommandManager provideCommandManager() {
        return new CommandManager(providePreferenceManager());
    }

    @Provides
    @Singleton
    MotionManager provideMotionManager() {
        return new MotionManager();
    }
}