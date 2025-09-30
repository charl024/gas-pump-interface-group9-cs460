package Screen;


import IOPort.PortLookupMap;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

//display needs to know message

public class ScreenMain extends Application {
    private ScreenDisplay screenDisplay;
    private HandleMessage handleMessage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        screenDisplay = new ScreenDisplay();
        screenDisplay.showScreen(primaryStage);
        screenDisplay.showPumpUnavailableScreen();

        handleMessage = new HandleMessage(screenDisplay);
        screenDisplay.setHandleMessage(handleMessage);
        primaryStage.setX(415);
        primaryStage.setY(0);
        primaryStage.show();
    }
}
