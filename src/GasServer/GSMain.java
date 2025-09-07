package GasServer;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GSMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        CommPort port = new CommPort(5);
        GasStation gasStation = new GasStation(port);


        Pane root = new Pane();
        root.getChildren().add(gasStation.getDisplay().getPane());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> {
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
