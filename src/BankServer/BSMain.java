package BankServer;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BSMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        CommPort port = new CommPort(4);
        BankServer bankServer = new BankServer(port);

        StackPane pane = new StackPane();
        pane.getChildren().add(bankServer.getDisplay().getPane());
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bank Server");

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        primaryStage.show();

        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10); // wait 10ms
                    Message message = port.get();         // blocking call
                    if (message != null) {
                        bankServer.getClient().handleMessage(message);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
