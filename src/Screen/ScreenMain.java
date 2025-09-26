package Screen;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.stage.Stage;

//display needs to know message

public class ScreenMain extends Application {
    private ScreenDisplay screenDisplay;
    private HandleMessage handleMessage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        screenDisplay = new ScreenDisplay();
        screenDisplay.showScreen(primaryStage);

        handleMessage = new HandleMessage(screenDisplay);

        primaryStage.show();
    }
}
