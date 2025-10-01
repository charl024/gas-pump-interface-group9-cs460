import IOPort.CommPort;
import IOPort.StatusPort;
import IOPort.IOPort;
import Util.Message;
import Util.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * ServerManager is responsible for managing communication
 * between the MainController and three external devices:
 * - Gas Server (GS)
 * - Bank Server (BS)
 * - Card Reader (CR)
 */
public class ServerManager implements Manager {
    private final CommPort gasServerPort;
    private final CommPort bankServerPort;
    private final StatusPort cardReaderPort;

    private boolean inTransaction;

    public ServerManager() {
        //When called, it should then create the IOConnections that will then
        // connect to the corresponding devices

        //Card reader won't receive any messages, just sends information out

        //Gas station server should receive input for when a gas transaction
        // is finished. Gas station can send out information about the gas
        // prices

        //Bank server will receive input of a credit card, it will determine
        // if its valid or not. After it determines validation, it will send
        // the information back
        gasServerPort = new CommPort(2);
        bankServerPort = new CommPort(1);
        cardReaderPort = new StatusPort(3);
    }

    @Override
    public List<IOPort> getPorts() {
        // Provide MainController with the list of ports this manager listens to
        return List.of(gasServerPort, bankServerPort, cardReaderPort);
    }

    @Override
    public List<Message> handleMessage(Message message) {
        System.out.printf("[ServerManager] Received: %s%n", message.getDescription());

        List<Message> toForward = new ArrayList<>();
        String[] parts = message.getDescription().split("-");

        // Decide how to handle based on the device prefix
        switch (parts[0]) {
            case "CR" -> {
                //Only request for bank to validate card if we aren't
                // currently in transaction
                if (!inTransaction) {
                    // Forward CardReader input to BankServer for validation
                    System.out.println("[ServerManager] CardReader input sending to BankServer");
                    Message toBank = new Message(message.getDescription());
                    toBank.changeDevice("BS");
                    bankServerPort.send(toBank);
                    toForward.add(new Message("SC-AUTHORIZING"));
                }
            }
            case "BS" -> {
                // Handle responses from BankServer (VALIDCARD / INVALIDCARD)
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
                        inTransaction = true;
                    }
                }
            }
            case "GS" -> {
                if (parts.length > 1 && (parts[1].equals("CHANGEPRICES") || parts[1].equals("INITIALPRICE"))) {
                    // Handle updates from GasServer (price updates)
                    System.out.println("[ServerManager] GasServer: price update");
                    Message toScreen = new Message(message.getDescription());
                    toScreen.changeDevice("SC");
                    toForward.add(toScreen);
                }
            }
        }

        // Return list of messages that MainController should route to other managers
        return toForward;
    }

    /**
     * Called by the controller to send commands to this manager's devices.
     *
     * @return
     */
    @Override
    public List<Message> sendMessage(Message message) {
        List<Message> toForward = new ArrayList<>();
        String[] parts = message.getDescription().split("-");
        switch (parts[0]) {
            case "GS" -> {
                //Gas station should only be receiving final transaction
                //messages, can reset in transaction
                inTransaction = false;
                if (!parts[1].equals("CANCELTRANSACTION")) {
                    System.out.printf("[ServerManager] Sending to GasServer: %s%n", message.getDescription());
                    gasServerPort.send(message);
                }
            }
            case "BS" -> {
                System.out.printf("[ServerManager] Sending to BankServer: %s%n", message.getDescription());
                bankServerPort.send(message);
            }
        }
        return toForward;
    }
}
