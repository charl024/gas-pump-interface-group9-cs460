/**
 * Handles user input when the screen is interacted with
 */
package CardReader;

import MessagePassed.Message;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Card Reader Input Handler
 */
public class CRInput {
    private CRIOClient client;
    private final CRDisplay display;

    /**
     * Base constructor used for demoing the GUI
     *
     * @param display Display
     */
    public CRInput(CRDisplay display) {
        this.display = display;
    }

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

    /**
     * Handles what happens when the card reader is tapped on in the demo
     */
    public void handleTapDemo() {
        System.out.println("Card reader clicked on!");
        display.updateStatusBox();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                display.finishCard();
            }
        }, 500);
    }
}
