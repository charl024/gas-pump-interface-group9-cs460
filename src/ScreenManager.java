import IOPort.CommPort;
import IOPort.IOPort;
import Util.Message;
import Util.Manager;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager implements Manager {
    private final CommPort screenServerPort;

    public ScreenManager() {
        screenServerPort = new CommPort(6);
    }

    @Override
    public List<IOPort> getPorts() {
        return List.of(screenServerPort);
    }

    @Override
    public List<Message> handleMessage(Message message) {
        System.out.printf("[ScreenManager] Received: %s%n", message.getDescription());

        List<Message> toForward = new ArrayList<>();
        String[] parts = message.getDescription().split("-");

        if (parts[0].equals("SC")) {
            String request = parts[1];
            switch (request) {
                case "GASINFO" -> {
                    System.out.println("[ScreenManager] Forwarding GASINFO request to GasServer");
                    Message toServer = new Message(message.getDescription());
                    toServer.changeDevice("GS");
                    toForward.add(toServer);
                }
                case "GASSELECTION" -> {
                    System.out.println("[ScreenManager] Forwarding GASSELECTION to PumpAssembly");
                    Message toPump = new Message(message.getDescription());
                    toPump.changeDevice("FM");
                    toForward.add(toPump);
                }
                case "NEWTOTAL" -> {
                    System.out.println("[ScreenManager] Forwarding NEWTOTAL to PumpAssembly");
                    Message toPump = new Message(message.getDescription());
                    toPump.changeDevice("FM");
                    toForward.add(toPump);
                }
            }
        }

        return toForward;
    }

    @Override
    public void sendMessage(Message message) {
        System.out.printf("[ScreenManager] Sending to Screen: %s%n", message.getDescription());
        screenServerPort.send(message);
    }
}
