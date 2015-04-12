package solutions.alterego.dronix.droidcar.utils;

import java.util.List;

import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Directions;

public class VoicePatternUtils {

    public static Directions RecognitionDirection(List<String> commands) {
        for (String s : commands)
            if (s.contains("destra"))
                return Directions.RIGHT;
            else if (s.contains("sinistra"))
                return Directions.LEFT;
            else if (s.contains("avanti"))
                return Directions.UP;
            else if (s.contains("indietro"))
                return Directions.DOWN;
        return null;
    }

    public static Brake RecognitionCommand(List<String> commands) {
        for (String s : commands)
            if (s.contains("stop"))
                return new Brake(Brake.FAST);
        return null;
    }



}
