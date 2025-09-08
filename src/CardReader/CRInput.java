/**
 * Handles user input when the screen is interacted with
 */
package CardReader;

import MessagePassed.Message;

/**
 * Card Reader Input Handler
 */
public class CRInput {
    private final CRIOClient client;
    private CRDisplay display;

    /**
     * Card Reader constructor
     *
     * @param client  Client that handles IO Messages
     * @param display Display that handles GUI interface for Card Reader
     */
    public CRInput(CRIOClient client, CRDisplay display) {
        this.client = client;
        this.display = display;
    }

    /**
     * Handles what happens when the use "taps their card" on the screen
     */
    public void handleTap() {
        //Send a message back to main saying that a card was tapped on screen
        //Main should indicate whether or not if the card is valid or not
        System.out.println("Clicked on");
        Message cardTap = new Message("CR-TAPPED"); //I have no idea if I like this for now
        client.sendMessage(cardTap);
    }
}
