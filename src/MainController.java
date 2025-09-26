import MessagePassed.Message;

public class MainController {
    private ServerManager serverManager;
    private ScreenManager screenManager;
    private GasStationManager gasStationManager;
    public MainController() {
        serverManager = new ServerManager(this);
        screenManager = new ScreenManager(this);

        //gasStationManager = new GasStationManager();
    }

    public void sendServerManagerMessage(Message message) {
        serverManager.messageRequest(message);
    }

    public void sendScreenManagerMessage(Message message) {

    }


    public void sendGasStationManagerMessage(Message message) {

    }
}
