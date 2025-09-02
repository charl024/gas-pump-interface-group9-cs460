package FlowMeter;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FMMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO IM GOING TO FIX THIS LATER
        Pane root = new Pane();
        root.setMinSize(410, 200);

        System.out.println("Checking if here");
        CommPort port = new CommPort(1);
        //Use localhost for hostName
        CommPort port2 = new CommPort(1);

        System.out.println("Check if here 2");
        Message testMessage = new Message("FM-2.86-10-15");
        System.out.println("here");
        FlowMeter flowMeter = new FlowMeter("localhost", 31);
        root.getChildren().add(flowMeter.getDisplay().getPane());

        //FMDisplay fmDisplay = new FMDisplay();
//        root.getChildren().add(fmDisplay.getPane());
        primaryStage.setScene(new Scene(root));
//        fmDisplay.setTimerRunning(true);
//        fmDisplay.setVolRate(10);
//        fmDisplay.setGasRate(3.5);
//        fmDisplay.startGasTimer();

        primaryStage.setResizable(false);
        primaryStage.setTitle("Flow Meter");

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit(); //Shut program completely if ran
        });

        primaryStage.show();


    }

    public static void main(String[] args) {


        launch(args);
    }
}
