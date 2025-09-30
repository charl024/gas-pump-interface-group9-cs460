import IOPort.CommPort;
import IOPort.IOPort;
import Util.Message;
import Util.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * ScreenManager is responsible for handling communication
 * between the MainController and the Screen device.
 * Responsibilities:
 *  - Listen for user input or requests coming from the Screen
 *  - Forward those requests to the appropriate manager (GasServer or PumpAssembly)
 *  - Deliver messages from other managers back to the Screen
 */
public class ScreenManager implements Manager {
    private final CommPort screenServerPort;

    public ScreenManager() {
        screenServerPort = new CommPort(6);
    }

    @Override
    public List<IOPort> getPorts() {
        // Provide MainController with the list of ports this manager listens to
        return List.of(screenServerPort);
    }

    @Override
    public List<Message> handleMessage(Message message) {
        System.out.printf("[ScreenManager] Received: %s%n", message.getDescription());

        List<Message> toForward = new ArrayList<>();
        String[] parts = message.getDescription().split("-");

        // Only process messages that originate from the screen
        if (parts[0].equals("SC")) {
            String request = parts[1];
            switch (request) {
                case "GASINFO" -> {
                    // User requests gas price info, sent to GasServer
                    System.out.println("[ScreenManager] Forwarding GASINFO request to GasServer");
                    Message toServer = new Message(message.getDescription());
                    toServer.changeDevice("GS");
                    toForward.add(toServer);
                }
                case "GASSELECTION" -> {
                    // User selects a gas type, sent to PumpAssembly
                    System.out.println("[ScreenManager] Forwarding GASSELECTION to PumpAssembly");
                    Message toPump = new Message(message.getDescription());
                    toPump.changeDevice("FM");
                    toForward.add(toPump);
                }
                case "NEWTOTAL" -> {
                    // Final totals request, sent to PumpAssembly to finish transaction
                    System.out.println("[ScreenManager] Forwarding NEWTOTAL to PumpAssembly");
                    Message toPump = new Message(message.getDescription());
                    toPump.changeDevice("FM");
                    toForward.add(toPump);
                }
            }
        }

        // Return any messages that need to be routed by MainController
        return toForward;
    }

    /**
     * Called by the controller to send commands to this manager's devices.
     *
     */
    @Override
    public List<Message> sendMessage(Message message) {
        List<Message> toForward = new ArrayList<>();
        // Send message directly to Screen device (output to GUI)
        System.out.printf("[ScreenManager] Sending to Screen: %s%n", message.getDescription());
        screenServerPort.send(message);
        return toForward;
    }
}
