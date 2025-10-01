import IOPort.CommPort;
import IOPort.IOPort;
import IOPort.StatusPort;
import Util.Message;
import Util.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * PumpAssembly is responsible for managing communication
 * between the MainController and two external devices:
 * - Flow Meter Pump (FM)
 * - Hose (HS)
 */
public class PumpAssemblyManager implements Manager {
    private final StatusPort hosePort;
    private final CommPort flowMeterPumpPort;

    private boolean hoseConnected = false;
    private boolean pumping = false;
    private boolean startedPumping = false;
    private boolean priceSelected = false;

    /**
     * Pump Assembly Manager constructor, handles messages for hose and
     * flow meter device
     */
    public PumpAssemblyManager() {
        hosePort = new StatusPort(5);
        flowMeterPumpPort = new CommPort(4);
    }

    /**
     * Get list of ports that manager will handle
     *
     * @return List of ports
     */
    @Override
    public List<IOPort> getPorts() {
        // Provide MainController with the list of ports this manager listens to
        return List.of(hosePort, flowMeterPumpPort);
    }

    /**
     * Handles incoming messages from the hose or flow meter.
     * May generate messages to forward to other managers (e.g., Screen, GasServer).
     *
     * @param message the incoming device message
     * @return messages that should be forwarded to other managers
     */
    @Override
    public List<Message> handleMessage(Message message) {
        System.out.printf("[PumpAssemblyManager] Received message: %s%n", message.getDescription());

        List<Message> toForward = new ArrayList<>();
        String[] parts = message.getDescription().split("-");
        String device = parts[0];

        switch (device) {
            case "HS" -> handleHoseMessage(parts, toForward);
            case "FM" -> handleFlowMeterMessage(parts, message, toForward);
        }

        return toForward;
    }

    /**
     * Handle hose status messages.
     * Controls pump state depending on connection/disconnection events.
     */
    private void handleHoseMessage(String[] parts, List<Message> toForward) {
        String hoseStatus = parts[1];
        hoseConnected = hoseStatus.equals("CN");
        System.out.printf("[PumpAssemblyManager] Hose status: %s (connected=%s)%n", hoseStatus, hoseConnected);

        if (priceSelected && hoseConnected && !pumping) {
            // Start pumping once gas is selected and hose is connected
            sendStartPump();
            toForward.add(new Message("SC-PUMPINGPROGRESS"));
        } else {
            if (pumping && !hoseConnected) {
                // Pause pump if hose disconnects mid-pump
                pumping = false;
                flowMeterPumpPort.send(new Message("FM-PAUSE"));
                System.out.println("[PumpAssemblyManager] Pump paused due to hose disconnect");
                toForward.add(new Message("SC-HOSEPAUSED"));
            }
            if (hoseConnected && !pumping && startedPumping) {
                // Resume pump automatically if hose reconnects mid-transaction
                pumping = true;
                flowMeterPumpPort.send(new Message("FM-RESUME"));
                System.out.println("[PumpAssemblyManager] Pump resumed after hose reconnect");
                toForward.add(new Message("SC-PUMPINGPROGRESS"));
            }
        }
    }

    /**
     * Handle flow meter messages (e.g., totals when pumping ends).
     * Forwards results to the Screen and Gas Server.
     */
    private void handleFlowMeterMessage(String[] parts, Message message, List<Message> toForward) {
        String flowMeterInfo = parts[1];
        System.out.printf("[PumpAssemblyManager] FlowMeter message: %s%n", flowMeterInfo);

        if (flowMeterInfo.equals("NEWTOTAL")) {
            // End of pumping session so reset state
            startedPumping = false;
            priceSelected = false;
            pumping = false;
            System.out.println("[PumpAssemblyManager] Pumping complete. Resetting state.");

            // Forward results to Screen
            Message toScreen = new Message(message.getDescription());
            toScreen.changeDevice("SC");
            toForward.add(toScreen);

            // Forward results to Gas Server
            Message toServer = new Message(message.getDescription());
            toServer.changeDevice("GS");
            toForward.add(toServer);
        }
    }

    /**
     * Called by the controller to send commands to this manager's devices.
     *
     * @return
     */
    @Override
    public List<Message> sendMessage(Message message) {
        System.out.printf("[PumpAssemblyManager] Sending to %s: %s%n", message.getDescription().split("-")[0], message.getDescription());

        List<Message> toForward = new ArrayList<>();

        String[] parts = message.getDescription().split("-");
        if (parts[0].equals("FM")) {
            if (parts[1].equals("GASSELECTION")) {
                priceSelected = true;
                System.out.println("[PumpAssemblyManager] Gas selection received, price set.");
                System.out.println("printing something");
                System.out.println(hoseConnected);
                if (hoseConnected) {
                    System.out.println("Reaching here");
                    toForward.addAll(handleMessage(new Message("HS-CN")));
                } else {
                    System.out.println("Reaching here20112");
                    toForward.add(new Message("SC-DC"));
                }

                toForward.addAll(handleMessage(message));
            }
            flowMeterPumpPort.send(message);
        }

        return toForward;
    }

    /**
     * Send a message to tell the flow meter to start flowing
     */
    private void sendStartPump() {
        Message startPump = new Message("FM-START");
        pumping = true;
        startedPumping = true;
        flowMeterPumpPort.send(startPump);
        System.out.println("[PumpAssemblyManager] Pump started.");
    }
}
