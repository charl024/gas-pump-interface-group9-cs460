package FlowMeter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FlowMeter extends Application {
    //When connection is made with IO, save port information and sockets in
    // order to send information back if needed
    public FlowMeter() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        root.setMinSize(410,200);
        FMDisplay fmDisplay = new FMDisplay();
        root.getChildren().add(fmDisplay.getPane());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
