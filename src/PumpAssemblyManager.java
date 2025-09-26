import IOPort.CommPort;
import IOPort.IOPort;
import IOPort.StatusPort;
import MessagePassed.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PumpAssemblyManager {
    private MainController mainController;
    private StatusPort hosePort;
    private CommPort flowMeterPumpPort;

    private ExecutorService executor;

    private boolean hoseConnected = false;
    private boolean pumping = false;
    private boolean startedPumping = false;

    public PumpAssemblyManager(MainController mainController) {
        this.mainController = mainController;
        hosePort = new StatusPort(5);
        flowMeterPumpPort = new CommPort(4);

        executor = Executors.newFixedThreadPool(2);
        start();
    }

    private void start() {
        executor.submit(() -> listenOnPort(hosePort));
        executor.submit(() -> listenOnPort(flowMeterPumpPort));
    }

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

                //When hose gets disconnected while in the middle of pumping,
                // we then need to send a message to the flow meter to also
                // pause

                if(pumping & !hoseConnected) {
                    pumping = false;
                    flowMeterPumpPort.send(new Message("FM-PAUSE"));
                }
                //if we get a message that hose is connected, and we already
                // started pumping, then we should automatically start pumping
                if(hoseConnected & !pumping & startedPumping) {
                    pumping = true;
                    flowMeterPumpPort.send(new Message("FM-RESUME"));
                }
            }
            case "FM" -> {
                //Types of messages that the flow meter can send to the
                // manager:
                //Gas total information, how much the gas cost, and how many
                // gallons was pumped total
                String flowMeterInfo = parts[1];
                if(flowMeterInfo.equals("TOTALS")) {
                    startedPumping = false;
                    //Send the totals over to the screen so that it can
                    // display it
                    message.changeDevice("SC");
                    mainController.sendScreenManagerMessage(message);
                }

            }
            default -> {

            }
        }
    }

    /**
     * Called after a user makes a gas selection
     */
    public void sendStartPump() {
        Message startPump = new Message("FM-START");
        pumping = true;
        startedPumping = true;
        flowMeterPumpPort.send(startPump);
    }


    private void listenOnPort(IOPort port) {
        while (!Thread.currentThread().isInterrupted()) {
            Message msg = null;
            if (port instanceof StatusPort) {
                msg = ((StatusPort) port).read();
            } else if (port instanceof CommPort) {
                msg = ((CommPort) port).get();
            }

            if (msg != null) {
                handleMessage(msg);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
