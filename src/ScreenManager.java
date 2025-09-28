/**
 * Screen manager, handles the screen device
 */

import IOPort.CommPort;
import MessagePassed.Message;


public class ScreenManager {
    private final MainController mainController;
    private final CommPort screenServerPort;

    /**
     * Constructor for Screen manager
     *
     * @param mainController Main controller that holds the other managers
     */
    public ScreenManager(MainController mainController) {
        this.mainController = mainController;
        screenServerPort = new CommPort(6);
        start();
    }

    /**
     * Create thread that handles messages between manager and screen
     */
    private void start() {
        Thread listenerThread = new Thread(() -> listenOnPort(screenServerPort));
        listenerThread.start();
    }

    /**
     * Handles messages that are received from the screen
     *
     * @param message Message that needs to be sent out to other managers
     */
    public void handleMessage(Message message) {
        String description = message.getDescription();
        String[] parts = description.split("-");

        if (parts[0].equals("SC")) {
            //Types of messages that the Screen will send out:
            //Users gas selection
            //Gas station prices request
            String request = parts[1];
            if (request.equals("GASINFO")) {
                message.changeDevice("GS");
                mainController.sendServerManagerMessage(message);
            } else if (request.equals("GASSELECTION")) {
                message.changeDevice("FM");
                mainController.sendPumpAssemblyManagerMessage(message);
            }
        }
    }

    /**
     * Handles messages that need to be sent to the screen
     *
     * @param message Message being sent
     */
    public void messageRequest(Message message) {
        screenServerPort.send(message);
    }

    /**
     * Listen to any new messages from IOPort
     *
     * @param port Port
     */
    private void listenOnPort(CommPort port) {
        while (!Thread.currentThread().isInterrupted()) {
            Message message = port.get();
            if (message != null) {
                handleMessage(message);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
