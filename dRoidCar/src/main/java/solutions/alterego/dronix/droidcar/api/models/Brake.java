package solutions.alterego.dronix.droidcar.api.models;

import lombok.Data;

@Data
public class Brake {

    public static final int SLOW = 0;

    public static final int FAST = 1;

    int intensity;

    public Brake(int intensity) {
        this.intensity = intensity;
    }
}
