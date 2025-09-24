/**
 * Class where IOPort and the Card Reader is created
 */
package CardReader;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

/**
 * Main for Card Reader
 */
public class CRMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        CardReader cardReader = new CardReader();
        createPane(primaryStage, cardReader);
        primaryStage.setTitle("Card Reader");

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();

//        if (arguments.size() == 1) {
//            CardReader cardReader = new CardReader();
//
//            createPane(primaryStage, cardReader);
//
//            primaryStage.setOnCloseRequest(event -> {
//                Platform.exit();
//            });
//
//            primaryStage.show();
//        } else if (arguments.isEmpty()) {
//
//            CommPort port = new CommPort(3);
//            CardReader cardReader = new CardReader(port);
//
//
//            createPane(primaryStage, cardReader);
//
//            primaryStage.setOnCloseRequest(event -> {
//                port.close();
//                Platform.exit();
//            });
//
//            primaryStage.show();
//
//            new Thread(() -> {
//                try {
//                    while (true) {
//                        Thread.sleep(10); // wait 10ms
//                        Message message = port.get();         // blocking call
//                        if (message != null) {
//                            cardReader.getClient().handleMessage(message);
//                        }
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        } else {
//            System.out.println("Invalid number of arguments");
//        }
    }

    /**
     * Creates the pane that will contain everything
     *
     * @param primaryStage Stage
     * @param cardReader   Card Reader
     */
    private void createPane(Stage primaryStage, CardReader cardReader) {
        StackPane pane = new StackPane();
        pane.setMinSize(400, 400);
        pane.setBackground(new Background(new BackgroundFill(Color.GREY,
                CornerRadii.EMPTY, Insets.EMPTY)));
        pane.getChildren().add(cardReader.getDisplay().getPane());
        pane.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle("Card Reader");
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
