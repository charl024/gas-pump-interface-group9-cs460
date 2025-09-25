/**
 * FlowMeter class, will simulate a flow meter in real life, keeping track of
 * how much gas and the cost of the gas as it is being pumped
 */
package FlowMeterPump;


import java.io.IOException;

/**
 * FlowMeter
 */
public class FlowMeter {

    private final FMDisplay display;
    private final FMIOClient client;
    private FMLServer server;

    /**
     * Main constructor that should be called when Flow meter needs to be
     * created
     */
    public FlowMeter() throws IOException {
        server = new FMLServer(4, this);
        display = new FMDisplay(server);
        client = new FMIOClient(display, server);

        new Thread(server).start();
    }

    /**
     * Get the display that shows the GUI
     *
     * @return Display
     */
    public FMDisplay getDisplay() {
        return display;
    }

    /**
     * Get the client that handles the IOPort messages
     *
     * @return Client
     */
    public FMIOClient getClient() {
        return client;
    }

}
