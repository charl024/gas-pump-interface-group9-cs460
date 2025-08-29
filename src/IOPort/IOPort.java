package IOPort;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IOPort {
    private ServerSocket serverSocket;
    private Socket controllerSocket;
    private Socket deviceSocket;

    /*
        A component that connects the main controller to other devices
        For example, we can "attach" the input of Controller to the output of Screen, or another device (and vice versa)
        More abstract, we can connect a Controller object with a Device object (Screen can be a child class of this)

        Just a placeholder for future stuff, change later to fit our needs
     */

    public IOPort(int connector) {

        // port is obtained from mapping connector to a port #, where port directly corresponds to a device
        int device_port = 0;

        try {
            serverSocket = new ServerSocket(device_port);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message) {

    }

    public String get() {
        return null;
    }

    public String read() {
        return null;
    }
}