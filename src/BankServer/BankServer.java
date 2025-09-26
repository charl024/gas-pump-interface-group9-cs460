/**
 * Bank server, creates the display and client to handle IOPort Messages
 */
package BankServer;

import IOPort.CommPort;
import IOPort.PortLookupMap;

import java.io.IOException;

/**
 * Bank Server
 */
public class BankServer {
    private final BSDisplay display;
    private final BSIOClient client;


    /**
     * Constructor for Bank Server
     */
    public BankServer() throws IOException {
        display = new BSDisplay();
        client = new BSIOClient(display);
        BSServer server = new BSServer(PortLookupMap.PortMap(1), this);

        //Start the server up
        new Thread(server).start();
    }

    /**
     * Get display that shows the GUI
     *
     * @return Display
     */
    public BSDisplay getDisplay() {
        return display;
    }

    /**
     * Return the client that handles messages between IOPort and the bank
     * server
     *
     * @return IOPort client
     */
    public BSIOClient getClient() {
        return client;
    }
}
