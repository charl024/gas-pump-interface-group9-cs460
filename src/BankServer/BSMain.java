/**
 * Main for Bank Server, creates the Bank Server and the IOPort to connect to
 */
package BankServer;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main
 */
public class BSMain extends Application {
    /**
     * Creates Bank server and connected IOPort
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception Errors
     */
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
            port.close();
            Platform.exit();
        });

        primaryStage.show();
        //Have a thread that just watches to see if there are any messages
        // from the connected IOPort
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

    /**
     * Main
     *
     * @param args Arguments (None)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
