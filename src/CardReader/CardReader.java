/**
 * Card Reader class, creates the display and the client that handles IOPort
 * messages
 */
package CardReader;

import java.io.IOException;

/**
 * Card Reader
 */
public class CardReader {
    private final CRDisplay display;
    private CRServer server;

    /**
     * Card Reader constructor
     *
     */
    public CardReader( ) throws IOException {
        server = new CRServer(3,this);
        display = new CRDisplay(server);

        new Thread(server).start();
    }

    /**
     * Get the display that shows the user interface
     *
     * @return Display
     */
    public CRDisplay getDisplay() {
        return display;
    }


}
