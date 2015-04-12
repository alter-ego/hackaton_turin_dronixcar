package solutions.alterego.dronix.droidcar.api;

import android.content.Context;

import javax.inject.Inject;

import retrofit.RestAdapter;
import rx.Observable;
import solutions.alterego.dronix.droidcar.R;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Direction;
import solutions.alterego.dronix.droidcar.api.models.Directions;
import solutions.alterego.dronix.droidcar.api.models.Speed;

public class CommandManager {

    private final CarCommandsService mCarCommandsService;

    @Inject
    public CommandManager(Context context ) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.api_endpoint))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mCarCommandsService = restAdapter.create(CarCommandsService.class);
    }

    public Observable<Speed> goTo(Directions direction) {
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
