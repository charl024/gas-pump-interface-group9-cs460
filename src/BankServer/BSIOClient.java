package BankServer;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Platform;

public class BSIOClient {
    private final BSDisplay display;
    private final CommPort port;

    public BSIOClient(BSDisplay display, CommPort port) {
        this.display = display;
        this.port = port;
    }

    public void handleMessage(Message message) {
        String receivedMessage = message.getDescription();
        String[] parts = receivedMessage.split("-");

        if (!(parts[0].equals("BS"))) {
            Message invalidMessage = new Message("BS-Invalid");
            sendMessage(invalidMessage);
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
            sendMessage(message);
        }
    }

    private void sendMessage(Message message) {
        port.send(message);
    }
}
