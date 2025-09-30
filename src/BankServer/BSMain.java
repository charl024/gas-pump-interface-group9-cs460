/**
 * Main for Bank Server, creates the Bank Server and the IOPort to connect to
 */
package BankServer;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;


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
    public void start(Stage primaryStage) throws IOException {
        BankServer bankServer = new BankServer();
        createPane(primaryStage, bankServer);

        primaryStage.setTitle("Bank Server");
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setX(71);
        primaryStage.setY(0);
        primaryStage.show();
    }

    /**
     * Create pane holding the GUI
     *
     * @param primaryStage Stage
     * @param bankServer   Bank Server
     */
    private void createPane(Stage primaryStage, BankServer bankServer) {
        StackPane pane = new StackPane();
        pane.getChildren().add(bankServer.getDisplay().getPane());
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bank Server");
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
