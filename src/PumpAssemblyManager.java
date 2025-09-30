/**
 * Pump Assembly Manager, handles Hose and Flowmeter/Pump
 */

import IOPort.CommPort;
import IOPort.IOPort;
import IOPort.StatusPort;
import MessagePassed.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Pump Assembly Manager
 */
public class PumpAssemblyManager {
    private final MainController mainController;
    private final StatusPort hosePort;
    private final CommPort flowMeterPumpPort;

    private final ExecutorService executor;

    private boolean hoseConnected = false;
    private boolean pumping = false;
    private boolean startedPumping = false;
    private boolean priceSelected = false;

    /**
     * Constructor for Pump Assembly Manager
     *
     * @param mainController
     */
    public PumpAssemblyManager(MainController mainController) {
        this.mainController = mainController;
        hosePort = new StatusPort(5);
        flowMeterPumpPort = new CommPort(4);

        executor = Executors.newFixedThreadPool(2);
        start();
    }

    /**
     * Start threads that will listen for any messages being sent from a device
     */
    private void start() {
        executor.submit(() -> listenOnPort(hosePort));
        executor.submit(() -> listenOnPort(flowMeterPumpPort));
    }

    /**
     * Handle message received from device
     *
     * @param message Message from device
     */
    public void handleMessage(Message message) {
        String description = message.getDescription();
        String[] parts = description.split("-");

        String deviceDescriptor = parts[0];

        switch (deviceDescriptor) {
            case "HS" -> {
                // hose always sends out a status message that tells us if it's disconnected or connected
                String hoseConnectionStatus = parts[1];
                // if we are connected, we are free to begin pumping
                // MainController will be the one checking this I assume
                hoseConnected = hoseConnectionStatus.equals("CN");
                System.out.println(hoseConnected);
                //When hose gets disconnected while in the middle of pumping,
                // we then need to send a message to the flow meter to also
                // pause


                //If price has been selected and the hose is connected, then
                // we need to start pumping
                if (priceSelected & hoseConnected & !pumping) {
                    System.out.println("Got in here");
                    sendStartPump();
                    mainController.sendScreenManagerMessage(new Message(
                            "SC-PUMPINGPROGRESS"));
                } else {
                    if (pumping & !hoseConnected) {
                        pumping = false;
                        flowMeterPumpPort.send(new Message("FM-PAUSE"));
                        mainController.sendScreenManagerMessage(new Message("SC-HOSEPAUSED"));
                    }
                    //if we get a message that hose is connected, and we already
                    // started pumping, then we should automatically start pumping
                    if (hoseConnected & !pumping & startedPumping) {
                        pumping = true;
                        flowMeterPumpPort.send(new Message("FM-RESUME"));
                        mainController.sendScreenManagerMessage(new Message(
                                "SC-PUMPINGPROGRESS"));
                    }
                }
            }
            case "FM" -> {
                //Types of messages that the flow meter can send to the
                // manager:
                //Gas total information, how much the gas cost, and how many
                // gallons was pumped total
                String flowMeterInfo = parts[1];
                if (flowMeterInfo.equals("NEWTOTAL")) {
                    startedPumping = false;
                    priceSelected = false;
                    //^should mean that pumping is over, so reset variables
                    //Send the totals over to the screen so that it can
                    // display it
                    message.changeDevice("SC");
                    mainController.sendScreenManagerMessage(message);

                    Message tempMessage = new Message(message.getDescription());
                    tempMessage.changeDevice("GS");
                    mainController.sendServerManagerMessage(tempMessage);
                }

            }
            default -> {

            }
        }
    }

    /**
     * Called after a user makes a gas selection so that pump can start
     */
    public void sendStartPump() {
        Message startPump = new Message("FM-START");
        pumping = true;
        startedPumping = true;
        flowMeterPumpPort.send(startPump);
    }

    /**
     * Handles messages that need to be sent to Flow meter and pump
     * (Should be called by outside managers)
     *
     * @param message (Message)
     */
    public void messageRequest(Message message) {
        String description = message.getDescription();
        String[] parts = description.split("-");
        if (parts[0].equals("FM")) {
            if (parts[1].equals("GASSELECTION")) {
                pumping = false;
                priceSelected = true;
                System.out.println("Reached selected");
            }
            System.out.println(message.getDescription());
            flowMeterPumpPort.send(message);
        }
    }

    /**
     * Handle messages being to IOPort
     *
     * @param port Port
     */
    private void listenOnPort(IOPort port) {
        Message lastMsg = null;

        while (!Thread.currentThread().isInterrupted()) {
            Message msg = null;

            if (port instanceof StatusPort) {
                msg = ((StatusPort) port).read();
            } else if (port instanceof CommPort) {
                msg = ((CommPort) port).get();
            }

            // Only process if it's new/different
            if (msg != null && (lastMsg == null || !msg.getDescription().equals(lastMsg.getDescription()))) {
                handleMessage(msg);
                lastMsg = msg;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
