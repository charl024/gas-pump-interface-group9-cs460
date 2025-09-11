package Hose;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Hose extends Application {

    @Override
    public void start(Stage stage) {
        HoseInternal hoseInternal = new HoseInternal();
        HoseDisplay hoseDisplay = new HoseDisplay(hoseInternal);
        Scene scene = new Scene(hoseDisplay);
        stage.setScene(scene);
        stage.setTitle("Hose");
        stage.setResizable(false);

        stage.setOnCloseRequest(e -> {
            hoseInternal.close();
            Platform.exit();
        });

        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
