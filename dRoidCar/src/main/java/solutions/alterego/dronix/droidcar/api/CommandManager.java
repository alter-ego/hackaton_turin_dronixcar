package solutions.alterego.dronix.droidcar.api;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import rx.Observable;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Direction;
import solutions.alterego.dronix.droidcar.api.models.Directions;
import solutions.alterego.dronix.droidcar.api.models.Speed;

public class CommandManager {

    private final CarCommandsService mCarCommandsService;

    public CommandManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.43.99:8888")
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
