/**
 * Main for Gas Station Server
 */
package GasServer;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

/**
 * Gas Station Main
 */
public class GSMain extends Application {
    /**
     * Creates the IOPort to connect to and the Gas Station Server
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception Errors
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> arguments = getParameters().getRaw();
        if (arguments.size() != 3) {
            System.out.println("Invalid input received, please give three arguments");
            System.exit(1);
        }

        double regular = 0;
        double plus = 0;
        double premium = 0;
        try {
            regular = Integer.parseInt(arguments.getFirst());
            plus = Integer.parseInt(arguments.get(1));
            premium = Integer.parseInt(arguments.get(2));
        } catch (NumberFormatException e) {
            System.out.println("Invalid arguments");
            System.exit(1);
        }

        GasStation gasStation = new GasStation(regular, plus, premium);
        createPane(primaryStage, gasStation);

        primaryStage.setTitle("Gas Station Server");
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setX(0);
        primaryStage.setY(317);
        primaryStage.show();
        System.out.println(primaryStage.getWidth());
        System.out.println(primaryStage.getHeight());
    }

    /**
     * Create pane that holds the whole GUI
     *
     * @param primaryStage Stage
     * @param gasStation   Gas Station
     */
    private void createPane(Stage primaryStage, GasStation gasStation) {
        Pane root = new Pane();
        root.getChildren().add(gasStation.getDisplay().getPane());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main
     *
     * @param args (None)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
