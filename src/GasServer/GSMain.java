/**
 * Main for Gas Station Server
 */
package GasServer;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Gas Station Main
 */
public class GSMain extends Application {
    /**
     * Creates the IOPort to connect to and the Gas Station Server
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception Errors
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        CommPort port = new CommPort(5);
        GasStation gasStation = new GasStation(port);


        Pane root = new Pane();
        root.getChildren().add(gasStation.getDisplay().getPane());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> {
            port.close();
            Platform.exit();
        });

        primaryStage.show();

        //Use this to show the GUI, then get the message from ioPort
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10); // wait 10ms
                    Message message = port.get();         // blocking call
                    if (message != null) {
                        gasStation.getClient().handleMessage(message);
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
