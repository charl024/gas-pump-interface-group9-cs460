/**
 * FlowMeter class, will simulate a flow meter in real life, keeping track of
 * how much gas and the cost of the gas as it is being pumped
 */
package FlowMeter;


/**
 * FlowMeter
 */
public class FlowMeter {

    private final FMDisplay display;
    private final FMIOClient client;

    /**
     * Main constructor that should be called when Flow meter needs to be
     * created
     */
    public FlowMeter() {
        display = new FMDisplay();
        client = new FMIOClient(display);
        display.setClient(client);
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
