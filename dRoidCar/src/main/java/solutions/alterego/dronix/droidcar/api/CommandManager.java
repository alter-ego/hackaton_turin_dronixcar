package solutions.alterego.dronix.droidcar.api;

import retrofit.RestAdapter;
import rx.Observable;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Direction;
import solutions.alterego.dronix.droidcar.api.models.Directions;
import solutions.alterego.dronix.droidcar.api.models.Speed;

public class CommandManager {
    public static final String URL ="http://192.168.43.98:8888";
    private final CarCommandsService mCarCommandsService;

    public CommandManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL)
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

    public Observable<Speed> getSpeed() {
        return mCarCommandsService.getSpeed();
    }
}
