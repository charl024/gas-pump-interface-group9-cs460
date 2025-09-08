/**
 * Main for Flow Meter/Pump
 * <p>
 * Creates the Devices and the IOPort to connect to
 */
package FlowMeterPump;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main
 */
public class FMMain extends Application {
    /**
     * Flow Meter/Pump Main, creates the Flow Meter/Pump and the IOPort to
     * connect to
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        //Create IO port first, then create the device

        CommPort port = new CommPort(2);
        FlowMeter flowMeter = new FlowMeter();
        flowMeter.getClient().setPort(port); //Save port to client


        //Adjusting size of pane
        Pane root = new Pane();
        root.setMinSize(410, 200);
        root.getChildren().add(flowMeter.getDisplay().getPane());
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Flow Meter");

        //If GUI is exited, turn program off
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        primaryStage.show();

        //Use this to show the GUI, then get the message from ioPort
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10); // wait 10ms
                    Message message = port.get();
                    if (message != null) {
                        flowMeter.getClient().handleMessage(message);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * Main
     * @param args (None)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
