/**
 * Handles user input when the screen is interacted with
 */
package CardReader;

import MessagePassed.Message;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Card Reader Input Handler
 */
public class CRInput {
    private final CRDisplay display;
    private final CRServer server;
    private boolean connected;

    /**
     * Constructor or Card reader input
     * @param server Server that card reader runs on
     * @param display Display that shows the Card Reader interface
     */
    public CRInput(CRServer server, CRDisplay display) {
        this.server = server;
        this.display = display;
        connected = false;
    }

    /**
     * Handles what happens when the use "taps their card" on the screen
     */
    public void handleTap() throws IOException {
        System.out.println("Clicked on");

        Random random = new Random();
        StringBuilder randomNums = new StringBuilder();
        //Create randoms sequence of 8 numbers
        for (int i = 0; i < 8; i++) {
            randomNums.append(random.nextInt(10));
        }

        Message cardTap = new Message("BS-" + randomNums);
        //Should only be able to tap the card if we have a client that can
        // handle the output from card reader
        if (connected) {
            server.sendMessage(cardTap);

            //Tapping the card animation
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

    /**
     * Called to indicate that a connection has been made with the server and
     * the device
     */
    public void connected() {
        connected = true;
    }
}
