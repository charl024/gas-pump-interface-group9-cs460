/**
 * Class where IOPort and the Card Reader is created
 */
package CardReader;

import IOPort.CommPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main for Card Reader
 */
public class CRMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        CommPort port = new CommPort(3);
        CardReader cardReader = new CardReader(port);


        StackPane pane = new StackPane();
        pane.setMinSize(400, 400);
        pane.setBackground(new Background(new BackgroundFill(Color.GREY,
                CornerRadii.EMPTY, Insets.EMPTY)));
        pane.getChildren().add(cardReader.getDisplay().getPane());
        pane.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(pane));

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        primaryStage.show();

    }

    /**
     * Main
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
