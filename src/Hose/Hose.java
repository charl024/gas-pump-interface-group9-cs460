package Hose;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Hose extends Application {

//    private boolean running = true;

    @Override
    public void start(Stage stage) {
        HoseInternal hoseInternal = new HoseInternal();
        HoseDisplay hoseDisplay = new HoseDisplay(hoseInternal);
        Scene scene = new Scene(hoseDisplay);
        stage.setScene(scene);
        stage.setTitle("Hose");
        stage.setResizable(false);

//        Thread t = new Thread(() -> {
//            while (running) {
//                boolean connectionStatus = hoseDisplay.isConnected();
//                if (connectionStatus) {
//                    hoseInternal.onConnect();
//                    hoseInternal.startInternalTimer();
//                } else {
//                    hoseInternal.onDisconnect();
//                }
//
//                hoseInternal.updateTimer();
//
//                System.out.println("Hose Connection Status: " + connectionStatus);
//
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

//        t.setDaemon(true);
//        t.start();

        stage.setOnCloseRequest(e -> {
//            running = false;
            hoseInternal.close();
            Platform.exit();
        });

        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
