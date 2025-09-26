import IOPort.CommPort;
import IOPort.IOPort;
import IOPort.StatusPort;
import MessagePassed.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PumpAssemblyManager {
    private StatusPort hosePort;
    private CommPort flowMeterPumpPort;

    private ExecutorService executor;

    private boolean hoseConnected = false;

    public PumpAssemblyManager() {
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
            }
            case "FM" -> {
                //TODO KYLE WHAT DOES FLOWMETER/PUMP NEED TO DO WITH ITS MESSAGES
            }
            default -> {

            }
        }
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
