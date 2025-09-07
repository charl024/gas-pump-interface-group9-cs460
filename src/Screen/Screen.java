package Screen;

import java.net.Socket;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.stage.Stage;


//Program for screen should start here

public class Screen  extends Application {
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


    public Screen(){
    }

    private void handleMessage(Message msg) {
        String messageStr = msg.getDescription();

        messageStr = "SC-i.4-12.4-re.9-3.6";

        String[] parts = messageStr.split("-");

        String deviceType = parts[0];
        if (!(deviceType.equals("SC"))) {
            Message invalidMessage = new Message("SC-Invalid");
            sendMessage(invalidMessage);
        } else {

            if (!parts[1].equals("*")) {
                String[] s = parts[1].split("\\.");
                screenDisplay.changeFont(s[0], Integer.parseInt(s[1]));
            }
            if (!parts[2].equals("*")) {
                String[] s = parts[2].split("\\.");
                screenDisplay.changeTextSize(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
            }
            if (!parts[3].equals("*")) {
                String[] s = parts[3].split("\\.");
                screenDisplay.changeButtonColor(
                        screenDisplay.convertColor(s[0]),
                        Integer.parseInt(s[1])
                );
                //TODO
                screenDisplay.changeButtonColorV2(
                        screenDisplay.convertColorV2(s[0]),
                        Integer.parseInt(s[1])
                );
                //TODO
            }
            if (!parts[4].equals("*")) {
                String[] s = parts[4].split("\\.");
                screenDisplay.giveButtonAction(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        screenDisplay = new ScreenDisplay();
        screenDisplay.showScreen(primaryStage);

        //TODO FOR CHARLES: PUT THREAD CODE BELOW HERE

        ////////////////////////////////////////
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
