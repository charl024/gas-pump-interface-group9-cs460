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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Flow Meter Display
 */
public class FMDisplay {
    private final FMLServer server;

    private final BorderPane pane; //Where all images/text will be placed on
    private final Label costInfo;
    private final Label volInfo;

    private double timePassed = 0.0;
    private long lastStartTime;

    private double curCost = 0;
    private double curVol = 0;
    private double totalVolume;

    private double gasRate; //How much gas costs per gallon
    private double volRate; //Rate gas is pumped into tank

    //Using this to replicate gas being pumped out
    private boolean timerRunning = false;
    private ScheduledExecutorService executor;


    //private final Pane pumpCord;
    private final String redCord = "-fx-background-color: red; -fx-pref-width: 300;" + " -fx-pref-height: 50;";
    private final String greenCord = "-fx-background-color: green; -fx-pref-width: " + "300; -fx-pref-height: 50;";
    private HBox pumpCord;
    private Pane pumpOne;
    private Pane pumpTwo;
    private Pane pumpThree;
    private Pane pumpFour;
    private Pane pumpFive;

    private VBox pumpButtons;
    private Button startPump;
    private Button stopPump;


    /**
     * Flow meter constructor, will create the text boxes and button needed
     * to simulate the flow meter
     */
    public FMDisplay(FMLServer server) {
        this.server = server;
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setMinSize(400, 200);


        costInfo = new Label("Cost: $" + curCost);
        costInfo.setFont(new Font(20));


        volInfo = new Label("Gallons:  " + curVol);
        volInfo.setFont(new Font(20));


        VBox info = new VBox(costInfo, volInfo);

        info.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        //info.setSpacing(1);
        info.setMinWidth(140);
        Pane paneHolder = new Pane(info);

        //Create pump that is just a blue square
        StackPane pump = new StackPane();
        pump.setStyle("-fx-background-color: lightblue; -fx-pref-width: 300; -fx-pref-height: 200;");
        Label pumpText = new Label("Pump");
        pump.getChildren().add(pumpText);


        createPumpPanes();
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
        //final long start = System.currentTimeMillis();

        executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            //If stopped, then ignore timer
            if (!timerRunning) {
                return;
            }


            long currentTime = System.currentTimeMillis();
            double elapsedSeconds = timePassed + (currentTime - lastStartTime) / 1000.0;


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
//                timerRunning = false;
                handleStop();
                sendGasTotals();
            }

        }, 0, 100, TimeUnit.MILLISECONDS); //100 ms

    }

    public void pauseTimer() {
        if(timerRunning) {
            long timeNow = System.currentTimeMillis();
            timePassed += (timeNow - lastStartTime) / 1000.0;
            timerRunning = false;
        }
        stopPump();
    }

    public void startTimer() {
        if(!timerRunning) {
            lastStartTime = System.currentTimeMillis();
            timerRunning = true;
        }
        startPump();
    }


    public void sendGasTotals() {
        //FM-TOTALS-TotalCost-TotalVolumes
        Message gasTotal = new Message("FM");
        double cost = curCost;
        double gallons = curVol;
        gasTotal.addToDescription("-TOTALS-" + cost + "-" + gallons);
        server.sendMessage(gasTotal);
    }

    /**
     * Called when the flow of the gas needed to be stopped
     */
    public void handleStop() {
        timerRunning = false;
        executor.shutdown();
        stopPump();
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
     * Start animation that makes it look like the pump is turning on
     */
    public void startPump() {
        //When the pump starts, create an animation like start for the pump
        ScheduledExecutorService executorPump = Executors.newScheduledThreadPool(1);
        Pane[] pumps = {pumpOne, pumpTwo, pumpThree, pumpFour, pumpFive};

        AtomicInteger count = new AtomicInteger(0);

        executorPump.scheduleAtFixedRate(() -> {
            int index = count.getAndIncrement();
            if (index < pumps.length) {
                pumps[index].setStyle(greenCord);
            } else {
                executorPump.shutdown();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Start animation that makes it look like the pump is turning off
     */
    public void stopPump() {
        //When the pump starts, create an animation like stop for the pump
        ScheduledExecutorService executorPump = Executors.newScheduledThreadPool(1);
        Pane[] pumps = {pumpFive, pumpFour, pumpThree, pumpTwo, pumpOne};

        AtomicInteger count = new AtomicInteger(0);

        executorPump.scheduleAtFixedRate(() -> {
            int index = count.getAndIncrement();
            if (index < pumps.length) {
                pumps[index].setStyle(redCord);
            } else {
                executorPump.shutdown();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Create panes needed for the animation
     */
    private void createPumpPanes() {
        pumpCord = new HBox();
        pumpOne = new Pane();
        pumpTwo = new Pane();
        pumpThree = new Pane();
        pumpFour = new Pane();
        pumpFive = new Pane();

        pumpOne.setStyle(redCord);
        pumpTwo.setStyle(redCord);
        pumpThree.setStyle(redCord);
        pumpFour.setStyle(redCord);
        pumpFive.setStyle(redCord);

        pumpOne.setMaxHeight(50);
        pumpTwo.setMaxHeight(50);
        pumpThree.setMaxHeight(50);
        pumpFour.setMaxHeight(50);
        pumpFive.setMaxHeight(50);

        pumpOne.setMaxWidth(40);
        pumpTwo.setMaxWidth(40);
        pumpThree.setMaxWidth(40);
        pumpFour.setMaxWidth(40);
        pumpFive.setMaxWidth(40);

        pumpCord.getChildren().addAll(pumpOne, pumpTwo, pumpThree, pumpFour, pumpFive);
        pumpCord.setAlignment(Pos.CENTER);
    }

    //TODO I have no idea if I want to keep this
    private void createButtons() {
        startPump = new Button("Start");
        stopPump = new Button("Stop");
        pumpButtons = new VBox();
        pumpButtons.setAlignment(Pos.TOP_CENTER);
        pumpButtons.getChildren().addAll(startPump, stopPump);
        pumpButtons.setSpacing(10);
        startPump.setOnAction(event -> startDemoPump());
        stopPump.setOnAction(event -> stopDemoPump());

        pane.setRight(pumpButtons);
    }

    /**
     * Start animation that makes pump look like its turning on
     */
    private void startDemoPump() {
        timerRunning = true;
        startPump();
        startGasTimer();
    }

    /**
     * Starts animation that makes pump look like its turning off
     */
    private void stopDemoPump() {
        timerRunning = false;
        executor.shutdown();
        stopPump();
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
