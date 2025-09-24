/**
 * Handles messages received by the IOport and handles sending messages back
 */
package CardReader;

import IOPort.CommPort;
import MessagePassed.Message;

//TODO LIKELY GOING TO DELETE THIS, NO POINT OF HAVING IT SINCE THE CARD
// READER SHOULDN'T BE RECEIVING ANYTHING

/**
 * Card Reader Client class
 */
public class CRIOClient {
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
    public void handleMessage(Message message) {
        //Two possible values that can happen: card is valid, or card isn't
        String receivedMessage = message.getDescription();
        String[] parts = receivedMessage.split("-");
        if (!parts[0].equals("CR")) {
            Message invalidMessage = new Message("CM-Invalid");
            sendMessage(invalidMessage);
        } else {
            String decision = parts[1];
            if (decision.equals("VALID")) {
                display.updateStatusBox();
                //TODO NOT SURE IF WE SHOULD BE RECEIVING A FINISHED MESSAGE OR JUST SIMPLY HAVE A TIMER TO RESET THE SQUARES
            } else if (decision.equals("FINISHED)")) { //Make it empty for next customer,
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
     * Set display
     *
     * @param display Card Reader Display
     */
    public void setDisplay(CRDisplay display) {
        this.display = display;
    }
}
