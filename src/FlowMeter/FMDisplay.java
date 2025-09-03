/**
 * Flow Meter Display class, will display the simulation of a flow meter
 */
package FlowMeter;

import MessagePassed.Message;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    private final FMUserInput FMUserInput;
    private final Button stopFuel;
    private final Label costInfo;
    private final Label volInfo;
    private VBox info;

    private double curCost = 0;
    private double curVol = 0;
    private double totalVolume;

    private double gasRate; //How much gas costs per gallon
    private double volRate; //Rate gas is pumped into tank

    //Using this to replicate gas being pumped out
    private boolean timerRunning = false;
    private ScheduledExecutorService executor;

    //Would an actual progress bar be helpful? If so, what is considered
    // being full/how would we know

    /**
     * Flow meter constructor, will create the text boxes and button needed
     * to simulate the flow meter
     */
    public FMDisplay() {
        pane = new BorderPane();
        pane.setMinSize(400, 200);
        FMUserInput = new FMUserInput(this);

        //pane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        stopFuel = new Button("Stop");
        stopFuel.setFocusTraversable(false);
        stopFuel.setOnAction(event -> FMUserInput.handleStop());

        costInfo = new Label("Cost: $" + curCost);
        costInfo.setFont(new Font(20));

        volInfo = new Label("Gallons:  " + curVol);
        volInfo.setFont(new Font(20));
        info = new VBox(costInfo, volInfo);

        pane.setCenter(info);
        pane.setRight(stopFuel);
        BorderPane.setAlignment(stopFuel, Pos.CENTER);
        info.setAlignment(Pos.CENTER);
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
        Message stopMessage = new Message("FM-Stopped");
        client.sendMessage(stopMessage); //COMMENT THIS OUT WHEN RUNNING GUI ALONE

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
