package solutions.alterego.dronix.droidcar.api;

import android.content.SharedPreferences;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import rx.Observable;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Direction;
import solutions.alterego.dronix.droidcar.api.models.Directions;
import solutions.alterego.dronix.droidcar.api.models.Server;
import solutions.alterego.dronix.droidcar.api.models.Speed;
import solutions.alterego.dronix.droidcar.utils.SharedPreferencesUtils;

public class CommandManager {

    private final CarCommandsService mCarCommandsService;
    SharedPreferences mSharedPreferences;

    public CommandManager(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        Server server =SharedPreferencesUtils.ReadConfig(mSharedPreferences);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + server.ip + ":" + server.port)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLog(new AndroidLog("DROIDCAR"))
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        mCarCommandsService = restAdapter.create(CarCommandsService.class);
    }

    public Observable<Speed> goTo(Directions direction) {
        Direction d = new Direction(direction.name().toLowerCase(), 0);
        return mCarCommandsService.goTo(d);
    }

    public Observable<Direction> getDirection() {
        return mCarCommandsService.getCurrentDirection();
    }

    public Observable<Speed> brake(Brake brake) {
        return mCarCommandsService.brake(brake);
    }

    public Observable<Speed> getSpeed() {
        return mCarCommandsService.getSpeed();
    }
}
