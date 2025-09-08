package Hose;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Hose extends Application {

    @Override
    public void start(Stage stage) {
        HoseDisplay hoseDisplay = new HoseDisplay();
        Scene scene = new Scene(hoseDisplay);
        stage.setScene(scene);
        stage.setTitle("Hose");
        stage.setResizable(false);

        System.out.println("Hose Internals setup");
        HoseInternal hoseInternal = new HoseInternal();

        System.out.println("Launching message sending thread.");

        new Thread(() -> {
            while (true) {
                boolean connectionStatus = hoseDisplay.isConnected();
                if (connectionStatus) {
                    hoseInternal.onConnect();
                } else {
                    hoseInternal.onDisconnect();
                }

                System.out.println("Hose Connection Status: " + connectionStatus);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
