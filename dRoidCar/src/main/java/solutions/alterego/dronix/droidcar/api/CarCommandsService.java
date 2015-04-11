package solutions.alterego.dronix.droidcar.api;

import android.database.Observable;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Direction;
import solutions.alterego.dronix.droidcar.api.models.Speed;

public interface CarCommandsService {

    @POST("/api/direction")
    Observable<Direction> goTo(@Body Direction direction);

    @GET("/api/direction")
    Observable<Direction> getCurrentDirection();

    @POST("/api/break")
    Observable<Speed> brake(@Body Brake brake);
}
