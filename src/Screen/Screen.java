package Screen;

import java.net.Socket;
import IOPort.CommPort;
import MessagePassed.Message;


//Program for screen should start here

public class Screen {
    private Socket socket;
    private String hostName;
    private CommPort port;


    //Use Object streams if we plan on passing objects around
    //private ObjectInputStream in;
    //private ObjectOutputStream out;

    //Use buffered reader/writer if just sending strings
    //private BufferedReader in;
    //private PrintWriter out;

    private ScreenDisplay screenDisplay;


    public Screen(ScreenDisplay screenDisplay) {
        this.screenDisplay = screenDisplay;

    }
    private void handleMessage(Message msg) {
        String messageStr = msg.getDescription();
        String[] parts = messageStr.split("-");

        String deviceType = parts[0];
        if (!(deviceType.equals("SC"))) {
            Message invalidMessage = new Message("SC-Invalid");
            sendMessage(invalidMessage);
        }else{
            if (parts.length > 1) {
               String font = parts[1];
                screenDisplay.changeFont(font);
            }

            if (parts.length > 2) {
                int size =  Integer.parseInt(parts[2]);
                screenDisplay. changeTextSize(size);
            }
            if (parts.length > 3) {
                int color = Integer.parseInt(parts[3]);
                screenDisplay.changeButtonColor(color);
            }
            if (parts.length > 4) {
                String action =  parts[4];
                screenDisplay.giveButtonAction(action);
            }







        }

    }
    public void messageReceived(Message msg) {
        handleMessage(msg);
    }
    public void sendMessage(Message msg) {
        port.send(msg);

    }
    public void setPort(CommPort port) {
        this.port = port;
    }


}
