package solutions.alterego.dronix.droidcar;

import android.app.Application;
import android.content.Context;

import solutions.alterego.dronix.droidcar.di.Component;

public class App extends Application {

    private Component component;

    public static Component component(Context context) {
        return ((App) context.getApplicationContext()).component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponentAndInject();
    }

    public void buildComponentAndInject() {
        component = Component.Initializer.init(this);
    }
}
