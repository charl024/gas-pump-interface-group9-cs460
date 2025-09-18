/**
 * Gas Station, creates the client for IOPort and display for the GUI
 */
package GasServer;

import IOPort.CommPort;

/**
 * Gas Station
 */
public class GasStation {
    private final GSDisplay display;
    private GSIOClient client;

    /**
     * Base constructor used for demoing the Gas Station
     */
    public GasStation() {
        display = new GSDisplay();
    }

    /**
     * Constructor for Gas Station Server, creates the display and client
     *
     * @param port Connected port
     */
    public GasStation(CommPort port) {
        display = new GSDisplay();
        client = new GSIOClient(display, port);
    }

    /**
     * Get the display
     *
     * @return Display
     */
    public GSDisplay getDisplay() {
        return display;
    }

    /**
     * Get the client
     *
     * @return Client
     */
    public GSIOClient getClient() {
        return client;
    }
}
