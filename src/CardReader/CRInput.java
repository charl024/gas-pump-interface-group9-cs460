/**
 * Handles user input when the screen is interacted with
 */
package CardReader;

import MessagePassed.Message;

import java.util.Random;

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


        Random random = new Random();
        StringBuilder randomNums = new StringBuilder();
        //Create randoms sequence of 8 numbers
        for (int i = 0; i < 8; i++) {
            randomNums.append(random.nextInt(10));
        }

        Message cardTap = new Message("BS-" + randomNums);
        client.sendMessage(cardTap);
    }
}
