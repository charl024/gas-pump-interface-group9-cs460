/**
 * Class where IOPort and the Card Reader is created
 */
package CardReader;

import IOPort.CommPort;
import MessagePassed.Message;
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
            port.close();
            Platform.exit();
        });

        primaryStage.show();

        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10); // wait 10ms
                    Message message = port.get();         // blocking call
                    if (message != null) {
                        cardReader.getClient().handleMessage(message);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

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
