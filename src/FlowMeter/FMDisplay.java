package FlowMeter;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Flow meter tells how much gas has been pumped
//When gas is finished pumping, and a new car starts pumping, reset volume
// count to zero
public class FMDisplay {
    private FMIOClient client;


    private final BorderPane pane; //Where all images/text will be placed on
    private final FMUserInput FMUserInput;
    private final Button stopFuel;
    private final Label costInfo;
    private final Label volInfo;
    private VBox info;

    private double cost = 0;
    private double curVol = 0;
    private double totalVolume;

    private double gasRate; //How much gas costs per gallon
    private double volRate; //Rate gas is pumped into tank

    //Using this to replicate gas being pumped out
    private boolean timerRunning = false;
    private ScheduledExecutorService executor;

    //Would an actual progress bar be helpful? If so, what is considered
    // being full/how would we know


    public FMDisplay() {
        pane = new BorderPane();
        pane.setMinSize(400, 200);
        FMUserInput = new FMUserInput(this);

        //pane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        stopFuel = new Button("Stop");
        stopFuel.setFocusTraversable(false);
        stopFuel.setOnAction(event -> FMUserInput.handleStop());

        costInfo = new Label("Cost: $" + cost);
        costInfo.setFont(new Font(20));

        volInfo = new Label("Gallons:  " + curVol);
        volInfo.setFont(new Font(20));
        info = new VBox(costInfo, volInfo);

        pane.setCenter(info);
        pane.setRight(stopFuel);
        BorderPane.setAlignment(stopFuel, Pos.CENTER);
        info.setAlignment(Pos.CENTER);

    }


    private void gasTimer() {
        //use a time that gets updated every millisecond?
        final long start = System.currentTimeMillis();

        executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            //If stopped, then ignore this
            if (!timerRunning) {
                return;
            }

            long elapsedTime = System.nanoTime() - start;
            double elapsedSeconds = elapsedTime / 1000.0; //convert mili to
            // seconds

            //initial delay, how often to repeat, time unit
            double gallons = elapsedSeconds * volRate;
            double cost = gallons * gasRate;
            updateGas(gallons, cost);

        }, 0, 100, TimeUnit.MILLISECONDS); //100 ms

    }

    //create methods that will update current cost and volumes
    public void handleStop() {
        timerRunning = false;
        executor.shutdown();
        System.out.println("Timer off");
    }


    private void updateGas(double volume, double cost) {
        volInfo.setText("Gallons: " + volume);
        costInfo.setText("Cost: $" + cost);
    }

    public void setClient(FMIOClient client) {
        this.client = client;
    }

    public BorderPane getPane() {
        return pane;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public double getGasRate() {
        return gasRate;
    }

    public void setGasRate(double gasRate) {
        this.gasRate = gasRate;
    }

    public double getVolRate() {
        return volRate;
    }

    public void setVolRate(double volRate) {
        this.volRate = volRate;
    }
}
