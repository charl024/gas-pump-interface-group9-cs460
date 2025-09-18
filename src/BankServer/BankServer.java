/**
 * Bank server, creates the display and client to handle IOPort Messages
 */
package BankServer;

import IOPort.CommPort;

/**
 * Bank Server
 */
public class BankServer {
    private final BSDisplay display;
    private BSIOClient client;

    /**
     * Base constructor used for demoing the Bank Server
     */
    public BankServer() {
        display = new BSDisplay();
    }

    /**
     * Constructor for Bank Server
     *
     * @param port Port connected to Bank Server
     */
    public BankServer(CommPort port) {
        display = new BSDisplay();
        client = new BSIOClient(display, port);
    }

    /**
     * Get display that shows the GUI
     * @return Display
     */
    public BSDisplay getDisplay() {
        return display;
    }

    /**
     * Return the client that handles messages between IOPort and the bank
     * server
     * @return IOPort client
     */
    public BSIOClient getClient() {
        return client;
    }
}
