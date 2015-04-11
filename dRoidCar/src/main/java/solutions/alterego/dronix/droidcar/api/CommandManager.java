package solutions.alterego.dronix.droidcar.api;

import android.database.Observable;

import retrofit.RestAdapter;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Direction;
import solutions.alterego.dronix.droidcar.api.models.Directions;
import solutions.alterego.dronix.droidcar.api.models.Speed;

public class CommandManager {

    private final CarCommandsService mCarCommandsService;

    public CommandManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.1.1")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mCarCommandsService = restAdapter.create(CarCommandsService.class);
    }

    public Observable<Direction> goTo(Directions direction) {
        Direction d = new Direction(direction.name(), 0);
        return mCarCommandsService.goTo(d);
    }

    public Observable<Direction> getDirection() {
        return mCarCommandsService.getCurrentDirection();
    }

    public Observable<Speed> brake(Brake brake) {
        return mCarCommandsService.brake(brake);
    }
}