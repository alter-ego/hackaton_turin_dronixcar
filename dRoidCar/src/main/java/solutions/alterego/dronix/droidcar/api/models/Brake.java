package solutions.alterego.dronix.droidcar.api.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(prefix = "m")
public class Brake {

    public static final int SLOW = 0;

    public static final int FAST = 1;

    int mIntensity;

    public Brake(int intensity) {
        mIntensity = intensity;
    }
}
