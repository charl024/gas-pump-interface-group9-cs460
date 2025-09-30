import IOPort.CommPort;
import IOPort.StatusPort;
import IOPort.IOPort;
import Util.Message;
import Util.Manager;

import java.util.ArrayList;
import java.util.List;

public class ServerManager implements Manager {
    private final CommPort gasServerPort;
    private final CommPort bankServerPort;
    private final StatusPort cardReaderPort;

    public ServerManager() {
        gasServerPort = new CommPort(2);
        bankServerPort = new CommPort(1);
        cardReaderPort = new StatusPort(3);
    }

    @Override
    public List<IOPort> getPorts() {
        return List.of(gasServerPort, bankServerPort, cardReaderPort);
    }

    @Override
    public List<Message> handleMessage(Message message) {
        System.out.printf("[ServerManager] Received: %s%n", message.getDescription());

        List<Message> toForward = new ArrayList<>();
        String[] parts = message.getDescription().split("-");

        switch (parts[0]) {
            case "CR" -> {
                System.out.println("[ServerManager] CardReader input sending to BankServer");
                Message toBank = new Message(message.getDescription());
                toBank.changeDevice("BS");
                bankServerPort.send(toBank);
                toForward.add(new Message("SC-AUTHORIZING"));
            }
            case "BS" -> {
                if (parts.length > 2) {
                    if (parts[2].equals("INVALIDCARD")) {
                        System.out.println("[ServerManager] BankServer: INVALIDCARD");
                        Message toScreen = new Message(message.getDescription());
                        toScreen.changeDevice("SC");
                        toForward.add(toScreen);
                    } else if (parts[2].equals("VALIDCARD")) {
                        System.out.println("[ServerManager] BankServer: VALIDCARD");
                        Message toScreen = new Message(message.getDescription());
                        toScreen.changeDevice("SC");
                        toForward.add(toScreen);
                    }
                }
            }
            case "GS" -> {
                if (parts.length > 1 &&
                        (parts[1].equals("CHANGEPRICES") || parts[1].equals("INITIALPRICE"))) {
                    System.out.println("[ServerManager] GasServer: price update");
                    Message toScreen = new Message(message.getDescription());
                    toScreen.changeDevice("SC");
                    toForward.add(toScreen);
                }
            }
        }

        return toForward;
    }

    @Override
    public void sendMessage(Message message) {
        String[] parts = message.getDescription().split("-");
        switch (parts[0]) {
            case "GS" -> {
                System.out.printf("[ServerManager] Sending to GasServer: %s%n", message.getDescription());
                gasServerPort.send(message);
            }
            case "BS" -> {
                System.out.printf("[ServerManager] Sending to BankServer: %s%n", message.getDescription());
                bankServerPort.send(message);
            }
        }
    }
}
