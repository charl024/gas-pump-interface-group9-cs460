/**
 * FlowMeter class, will simulate a flow meter in real life, keeping track of
 * how much gas and the cost of the gas as it is being pumped
 */
package FlowMeter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FlowMeter
 */
public class FlowMeter extends Application {
    //When connection is made with IO, save port information and sockets in
    // order to send information back if needed

    //I think when flow meter is created, it should be given the information
    // for the IO port it needs to connect to.

    //This class should be called first, so that it can do create the GUI's
    // and what not first
    private FMDisplay display;
    private FMIOClient client;

    private String hostName;
    private int portNumber;

    //Needed to test program by itself,
    public FlowMeter() {

    }

    /**
     * Main constructor that should be called when Flow meter needs to be
     * created
     *
     * @param hostName   Host name for application
     * @param portNumber Port number for application
     */
    //Used when Main main calls/creates this
    public FlowMeter(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        display = new FMDisplay();
        //I need the client to know about the display so that it can update the GUI
        client = new FMIOClient(hostName, portNumber, display);
        display.setClient(client);

    }

    //USING THIS TO TEST GUI ONLY
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        root.setMinSize(410, 200);
        FMDisplay fmDisplay = new FMDisplay();
        root.getChildren().add(fmDisplay.getPane());
        primaryStage.setScene(new Scene(root));
        fmDisplay.setTimerRunning(true);
        fmDisplay.setVolRate(10);
        fmDisplay.setGasRate(3.5);
        fmDisplay.startGasTimer();

        primaryStage.setResizable(false);
        primaryStage.setTitle("Flow Meter");

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit(); //Shut program completely if ran
        });

        primaryStage.show();

        //WHEN PROGRAM IS EXITED, TURN OFF PROGRAM
    }

    public static void main(String[] args) {
        launch(args);
    }
}
