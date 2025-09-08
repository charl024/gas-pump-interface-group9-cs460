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
    private final BSIOClient client;

    /**
     * Constructor for Bank Server
     * @param port Port connected to Bank Server
     */
    public BankServer(CommPort port) {
        display = new BSDisplay();
        client = new BSIOClient(display, port);
    }

    public BSDisplay getDisplay() {
        return display;
    }

    public BSIOClient getClient() {
        return client;
    }
}
