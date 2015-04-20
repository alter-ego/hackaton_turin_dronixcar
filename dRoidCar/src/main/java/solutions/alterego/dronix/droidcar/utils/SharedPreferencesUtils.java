package solutions.alterego.dronix.droidcar.utils;

import android.content.SharedPreferences;

import solutions.alterego.dronix.droidcar.api.models.Server;

public class SharedPreferencesUtils {

    public static Void WriteConfig(SharedPreferences sharedPref, Server server){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Ip", server.ip);
        editor.putString("Port", server.port);
        editor.putString("PortCamera", server.portCamera);
        editor.apply();
        return null;
    }

    public static Server ReadConfig(SharedPreferences sharedPref){
        return new Server(sharedPref.getString("Ip",""), sharedPref.getString("Port",""), sharedPref.getString("PortCamera",""));
    }

}
