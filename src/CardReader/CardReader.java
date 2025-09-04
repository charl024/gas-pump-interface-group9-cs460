/**
 * Card Reader class, creates the display and the client that handles IOPort
 * messages
 */
package CardReader;

import IOPort.CommPort;

/**
 * Card Reader
 */
public class CardReader {
    private final CRDisplay display;
    private CRIOClient client;

    /**
     * Card Reader constructor
     * @param port Port that will be connected to the device
     */
    public CardReader(CommPort port) {
        client = new CRIOClient(port);
        display = new CRDisplay(client);
        client.setDisplay(display);
    }

    /**
     * Get the display that shows the user interface
     * @return Display
     */
    public CRDisplay getDisplay() {
        return display;
    }

}
