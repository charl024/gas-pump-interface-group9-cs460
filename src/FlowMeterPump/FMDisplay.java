/**
 * Flow Meter Display class, will display the simulation of a flow meter
 */
package FlowMeterPump;

import MessagePassed.Message;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Flow Meter Display
 */
public class FMDisplay {
    private FMIOClient client;

    private final BorderPane pane; //Where all images/text will be placed on
    private final Label costInfo;
    private final Label volInfo;

    private double curCost = 0;
    private double curVol = 0;
    private double totalVolume;

    private double gasRate; //How much gas costs per gallon
    private double volRate; //Rate gas is pumped into tank

    //Using this to replicate gas being pumped out
    private boolean timerRunning = false;
    private ScheduledExecutorService executor;


    private final Pane pumpCord;
    private final String redCord = "-fx-background-color: red; -fx-pref-width: 300;" +
            " -fx-pref-height: 50;";


    /**
     * Flow meter constructor, will create the text boxes and button needed
     * to simulate the flow meter
     */
    public FMDisplay() {
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setMinSize(400, 200);


        costInfo = new Label("Cost: $" + curCost);
        costInfo.setFont(new Font(20));


        volInfo = new Label("Gallons:  " + curVol);
        volInfo.setFont(new Font(20));


        VBox info = new VBox(costInfo, volInfo);

        info.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        )));
        //info.setSpacing(1);
        info.setMinWidth(140);
        Pane paneHolder = new Pane(info);

        //Create pump that is just a blue square
        StackPane pump = new StackPane();
        pump.setStyle("-fx-background-color: lightblue; -fx-pref-width: 300; -fx-pref-height: 200;");
        Label pumpText = new Label("Pump");
        pump.getChildren().add(pumpText);

        //Creat the cord that will change color based on the status of the tank
        pumpCord = new Pane();
        pumpCord.setStyle(redCord);
        pumpCord.setMaxHeight(50);
        HBox pumpStuff = new HBox(pump, pumpCord);
        pumpStuff.setAlignment(Pos.CENTER);
        pane.setTop(paneHolder);
        pane.setCenter(pumpStuff);
    }

    /**
     * Called when the gas has started to begun pumping. While the gas is
     * being pumped, the amount of gas that is pumped and the cost of the gas
     * will be updated every second
     */
    public void startGasTimer() {

        final long start = System.currentTimeMillis();

        executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            //If stopped, then ignore timer
            if (!timerRunning) {
                return;
            }


            long elapsedTime = System.currentTimeMillis() - start;
            double elapsedSeconds = elapsedTime / 1000.0; //convert milliseconds to seconds

            //initial delay, how often to repeat, time unit
            double gallons = elapsedSeconds * (volRate / 60);
            curVol = gallons;


            double cost = gallons * gasRate;
            curCost = cost;

            Platform.runLater(() -> {
                updateGas(gallons, cost);
            });
            //If current volume is greater than total volume, then we are done
            if (curVol > totalVolume) {
                timerRunning = false;
            }

        }, 0, 100, TimeUnit.MILLISECONDS); //100 ms

    }

    /**
     * Called when the flow of the gas needed to be stopped
     */
    public void handleStop() {
        timerRunning = false;
        executor.shutdown();
        Message stopMessage = new Message("FM-PUMPSTOP");
        client.sendMessage(stopMessage); //COMMENT THIS OUT WHEN RUNNING GUI ALONE
        setRedCord();
        System.out.println("Timer off");
    }

    /**
     * Updates the gas information that needs to be displayed
     *
     * @param volume How much gas has been pumped out so far
     * @param cost   Cost of the gas pumped out so far
     */
    private void updateGas(double volume, double cost) {
        String format = String.format("%.2f", volume);
        volInfo.setText("Gallons: " + format);
        format = String.format("%.2f", cost);
        costInfo.setText("Cost: $" + format);
    }

    public void setGreenCord() {
        String greenCord = "-fx-background-color: green; -fx-pref-width: " +
                "300; -fx-pref-height: 50;";
        pumpCord.setStyle(greenCord);
    }

    public void setRedCord() {
        pumpCord.setStyle(redCord);
    }

    /**
     * Get client class that is connected to the IOport
     *
     * @param client FMIO Client
     */
    public void setClient(FMIOClient client) {
        this.client = client;
    }

    /**
     * Get the main pane that the display uses
     *
     * @return Pane
     */
    public BorderPane getPane() {
        return pane;
    }

    /**
     * Check if the timer is running
     *
     * @return True if timer running, False if not
     */
    public boolean isTimerRunning() {
        return timerRunning;
    }

    /**
     * Set the cost per gallon
     *
     * @param gasRate Cost per gallon
     */
    public void setGasRate(double gasRate) {
        this.gasRate = gasRate;
    }

    /**
     * Set how much gallons are pumped per minute
     *
     * @param volRate Gallons per minute
     */
    public void setVolRate(double volRate) {
        this.volRate = volRate;
    }

    /**
     * Set total gallons that a car can transport
     *
     * @param totalVolume Total gallons
     */
    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    /**
     * Turn timer on or off
     *
     * @param timerRunning True if timer on, False if not
     */
    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }
}
