package Screen;

import java.net.Socket;
//Program for screen should start here

public class Screen {
    private Socket socket;
    private String hostName;
    private int port;

    //Use Object streams if we plan on passing objects around
    //private ObjectInputStream in;
    //private ObjectOutputStream out;

    //Use buffered reader/writer if just sending strings
    //private BufferedReader in;
    //private PrintWriter out;

    private ScreenDisplay screenDisplay;

    public Screen() {

    }

}
