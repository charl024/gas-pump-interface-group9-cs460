/**
 * Gas Station, creates the client for IOPort and display for the GUI
 */
package GasServer;

import IOPort.CommPort;

import java.io.IOException;

/**
 * Gas Station
 */
public class GasStation {
    private final GSDisplay display;
    private GSIOClient client;

    /**
     * Constructor for Gas Station Server, creates the display and client
     *
     */
    public GasStation(double reg, double plus, double prem) throws IOException {
        GSServer server = new GSServer(2,this);
        display = new GSDisplay();
        client = new GSIOClient(display, server);

        display.updatePriceInput(reg, plus, prem);
        new Thread(server).start();
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
