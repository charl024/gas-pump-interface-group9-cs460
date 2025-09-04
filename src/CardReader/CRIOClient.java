/**
 * Handles messages received by the IOport and handles sending messages back
 */
package CardReader;

import IOPort.CommPort;
import MessagePassed.Message;
import Observer.Listener;

/**
 * Card Reader Client class
 */
public class CRIOClient implements Listener {
    private final CommPort port;
    private CRDisplay display;

    /**
     * Constructor for Card Reader Client class
     *
     * @param port Port that is supposed to be connected to the device
     */
    public CRIOClient(CommPort port) {
        this.port = port;
    }

    /**
     * Handles messages received from the IOPort
     *
     * @param message Message received
     */
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

    /**
     * Send message back to the IOPort
     *
     * @param message Message being sent back
     */
    public void sendMessage(Message message) {
        port.send(message);
    }

    /**
     * Method called to notify the device that a new message has been received
     *
     * @param message Message that was just received
     */
    @Override
    public void messageReceived(Message message) {
        handleMessage(message);
    }

    /**
     * Set display
     *
     * @param display Card Reader Display
     */
    public void setDisplay(CRDisplay display) {
        this.display = display;
    }
}
