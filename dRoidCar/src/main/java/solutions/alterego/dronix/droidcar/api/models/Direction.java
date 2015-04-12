package solutions.alterego.dronix.droidcar.api.models;

import lombok.Data;

@Data
public class Direction {

    String direction;

    int speed;

    public Direction(String direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }
}
