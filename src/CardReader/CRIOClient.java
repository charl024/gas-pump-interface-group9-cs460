package CardReader;

import IOPort.CommPort;
import MessagePassed.Message;
import Observer.Listener;

public class CRIOClient implements Listener {
    private final CommPort port;
    private CRDisplay display;

    public CRIOClient(CommPort port) {
        this.port = port;
    }

    private void handleMessage(Message message) {
        //Two possible values that can happen: card is valid, or card isn't
        String receivedMessage = message.getDescription();
        String[] parts = receivedMessage.split("-");
        if (!parts[0].equals("CR")) {
            Message invalidMessage = new Message("CM-Invalid");
            sendMessage(invalidMessage);
        } else {
            String decision = parts[1];
            if (decision.equals("VALID")) {
                display.validCard();
            } else if (decision.equals("INVALID")) {
                display.invalidCard();
            } else if (decision.equals("FINISHED)")) { //Make it empty for next customer
                display.finishCard();
            }
        }
    }

    public void sendMessage(Message message) {
        port.send(message);
    }

    @Override
    public void messageReceived(Message message) {
        handleMessage(message);
    }

    public void setDisplay(CRDisplay display) {
        this.display = display;
    }
}
