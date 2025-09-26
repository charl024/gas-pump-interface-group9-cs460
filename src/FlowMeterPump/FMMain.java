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

import java.io.IOException;
import java.util.List;

/**
 * Main
 */
public class FMMain extends Application {
    /**
     * Flow Meter/Pump Main, creates the Flow Meter/Pump and the IOPort to
     * connect to
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FlowMeter flowMeter = new FlowMeter();
        createPane(primaryStage, flowMeter);

        primaryStage.setTitle("FlowMeter-Pump");
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    /**
     * Create the pane that holds the GUI
     *
     * @param primaryStage Stage
     * @param flowMeter    Flow Meter
     */
    private void createPane(Stage primaryStage, FlowMeter flowMeter) {
        Pane root = new Pane();
        root.setMinSize(410, 200);
        root.getChildren().add(flowMeter.getDisplay().getPane());
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Flow Meter");
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
