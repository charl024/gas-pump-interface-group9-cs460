/**
 * Bank Server IOPort Client, handles messages received and messages that need
 * to be sent
 */
package BankServer;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Platform;

/**
 * Bank Server IOPort Client
 */
public class BSIOClient {
    private final BSDisplay display;

    /**
     * Constructor for Bank Server Client
     *
     * @param display Display that creates the GUI
     */
    public BSIOClient(BSDisplay display) {
        this.display = display;
    }

    /**
     * Handle messages received from the connected IOPort
     *
     * @param message Message received
     */
    public Message handleMessage(Message message) {
        String receivedMessage = message.getDescription();
        String[] parts = receivedMessage.split("-");

        if (!(parts[0].equals("BS"))) {
            Message invalidMessage = new Message("BS-Invalid");
            return invalidMessage;
        } else {
            String numValue = parts[1];
            //Just going to assume that the length of the number is 8 numbers
            // long

            //criteria for the credit card to be denied:
            //if the number contains the digits 4,6,0 where the order
            //doesn't matter. or if the sum of the numbers is equal to 46

            //I have no idea how often this will go off

            int total = 0;
            for (char letter : numValue.toCharArray()) {
                total += letter - '0';
            }

            if ((numValue.contains("4") && numValue.contains("6") && numValue.contains("0")) || (total == 46)) {
                message.addToDescription("-INVALIDCARD");
                Platform.runLater(() -> {
                    display.updateInvalidCard(message);
                });

            } else {
                message.addToDescription("-VALIDCARD");
                Platform.runLater(() -> {
                    display.updateValidCard(message);
                });

            }
            return message;

        }
    }
}
