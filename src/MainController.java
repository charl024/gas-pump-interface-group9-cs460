import MessagePassed.Message;

public class MainController {
    private final ServerManager serverManager;
    private final ScreenManager screenManager;
    private final PumpAssemblyManager pumpAssembly;

    public MainController() {
        serverManager = new ServerManager(this);
        screenManager = new ScreenManager(this);
        pumpAssembly = new PumpAssemblyManager(this);

    }

    public void sendServerManagerMessage(Message message) {
        serverManager.messageRequest(message);
    }

    public void sendScreenManagerMessage(Message message) {
        screenManager.messageRequest(message);
    }

    public void sendPumpAssemblyManagerMessage(Message message) {
        pumpAssembly.messageRequest(message);
    }

    public static void main(String[] args) {
        MainController controller = new MainController();
    }
}
