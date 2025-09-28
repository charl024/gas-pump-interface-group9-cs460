/**
 * Main controller, creates all the needed managers
 */

import MessagePassed.Message;

/**
 * Main controller
 */
public class MainController {
    private final ServerManager serverManager;
    private final ScreenManager screenManager;
    private final PumpAssemblyManager pumpAssembly;

    /**
     * Main controller constructor, creates the needed managers
     */
    public MainController() {
        screenManager = new ScreenManager(this);
        pumpAssembly = new PumpAssemblyManager(this);
        serverManager = new ServerManager(this);
    }

    /**
     * Called when a message needs to be sent to the Server manager
     *
     * @param message Message being sent
     */
    public void sendServerManagerMessage(Message message) {
        serverManager.messageRequest(message);
    }

    /**
     * Called when a message needs to be sent to the Screen manager
     *
     * @param message Message being sent
     */
    public void sendScreenManagerMessage(Message message) {
        screenManager.messageRequest(message);
    }

    /**
     * Called when a message needs to be sent to the Pump Assembly manager
     *
     * @param message Message being sent
     */
    public void sendPumpAssemblyManagerMessage(Message message) {
        pumpAssembly.messageRequest(message);
    }

    public static void main(String[] args) {
        MainController controller = new MainController();
    }
}
