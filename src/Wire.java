import java.net.ServerSocket;
import java.net.Socket;

public class Wire {
    // some example fields of what it might have, thoughts?
    ServerSocket serverSocket;
    Socket controllerSocket;
    Socket deviceSocket;
    /*
        A component that connects the main controller to other devices
        For example, we can "attach" the input of Controller to the output of Screen, or another device (and vice versa)
        More abstract, we can connect a Controller object with a Device object (Screen can be a child class of this)

        Just a placeholder for future stuff, change later to fit our needs
     */
}
