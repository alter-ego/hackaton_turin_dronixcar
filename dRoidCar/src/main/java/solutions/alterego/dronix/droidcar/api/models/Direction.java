package solutions.alterego.dronix.droidcar.api.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m")
public class Direction {

    String mDirection;

    int mSpeed;

    public Direction(String direction, int speed) {
        mDirection = direction;
        mSpeed = speed;
    }
}
