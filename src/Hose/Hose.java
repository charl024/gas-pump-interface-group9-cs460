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
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
