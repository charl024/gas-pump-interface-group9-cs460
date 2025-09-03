package FlowMeter;

import IOPort.CommPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FMMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Create IO port first, then create the device

        CommPort port = new CommPort(2);
        FlowMeter flowMeter = new FlowMeter();
        port.setDevice(flowMeter.getClient());//Save client to port
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
        //TODO TO UPDATE HOW TO HANDLE MESSAGES SENT FROM MAIN!
        port.get();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
