package solutions.alterego.dronix.droidcar.api.models;

public class Server {
    public final String ip, port, portCamera;

    public Server(String ip, String port, String portCamera){
        this.ip = ip;
        this.port = port;
        this.portCamera = portCamera;
    }
}
