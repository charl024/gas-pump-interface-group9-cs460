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

import java.util.List;

/**
 * Gas Station Main
 */
public class GSMain extends Application {
    /**
     * Creates the IOPort to connect to and the Gas Station Server
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception Errors
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> arguments = getParameters().getRaw();
        if (arguments.size() == 1) {
            GasStation gasStation = new GasStation();
            gasStation.getDisplay().setDemoPrices();
            createPane(primaryStage, gasStation);

            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
            });
        } else {
            CommPort port = new CommPort(5);
            GasStation gasStation = new GasStation(port);


            createPane(primaryStage, gasStation);


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
    }

    /**
     * Create pane that holds the whole GUI
     *
     * @param primaryStage Stage
     * @param gasStation   Gas Station
     */
    private void createPane(Stage primaryStage, GasStation gasStation) {
        Pane root = new Pane();
        root.getChildren().add(gasStation.getDisplay().getPane());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main
     *
     * @param args (None)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
