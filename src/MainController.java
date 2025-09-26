import MessagePassed.Message;

public class MainController {
    private ServerManager serverManager;
    private ScreenManager screenManager;
    private PumpAssemblyManager pumpAssembly;
    public MainController() {
        serverManager = new ServerManager(this);
        screenManager = new ScreenManager(this);
        pumpAssembly = new PumpAssemblyManager(this);

    }

    public void sendServerManagerMessage(Message message) {
        serverManager.messageRequest(message);
    }

    public void sendScreenManagerMessage(Message message) {

    }


    public void sendGasStationManagerMessage(Message message) {

    }

    public void sendPumpAssemblyManagerMessage(Message message) {

    }

    public static void main(String[] args) {
        MainController controller = new MainController();
    }
}
