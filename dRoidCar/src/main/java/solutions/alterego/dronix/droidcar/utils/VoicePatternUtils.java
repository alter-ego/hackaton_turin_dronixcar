package solutions.alterego.dronix.droidcar.utils;

import android.content.Context;
import android.content.res.Resources;

import java.util.List;

import solutions.alterego.dronix.droidcar.R;
import solutions.alterego.dronix.droidcar.api.models.Brake;
import solutions.alterego.dronix.droidcar.api.models.Directions;

public class VoicePatternUtils {

    public static Directions RecognitionDirection(List<String> commands, Resources resources) {
        for (String s : commands)
            if (s.contains(resources.getString(R.string.right)))
                return Directions.RIGHT;
            else if (s.contains(resources.getString(R.string.left)))
                return Directions.LEFT;
            else if (s.contains(resources.getString(R.string.forward)))
                return Directions.UP;
            else if (s.contains(resources.getString(R.string.back)))
                return Directions.DOWN;
        return null;
    }

    public static Brake RecognitionCommand(List<String> commands, Resources resources) {
        for (String s : commands)
            if (s.contains(resources.getString(R.string.stop)))
                return new Brake(Brake.FAST);
        return null;
    }



}
