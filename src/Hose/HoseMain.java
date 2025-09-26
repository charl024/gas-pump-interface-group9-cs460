package Hose;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HoseMain extends Application {
    private final int hoseConnector = 5;

    @Override
    public void start(Stage stage) {
        HoseIOServer hoseServer = new HoseIOServer(hoseConnector);
        hoseServer.start();

        HoseIOClient hoseClient = new HoseIOClient(hoseConnector);
        HoseDisplay hoseDisplay = new HoseDisplay(hoseClient);
        Scene scene = new Scene(hoseDisplay);
        stage.setScene(scene);
        stage.setTitle("Hose");
        stage.setResizable(false);

        stage.setOnCloseRequest(e -> {
            hoseClient.close();
            Platform.exit();
        });

        stage.show();


    }
}
