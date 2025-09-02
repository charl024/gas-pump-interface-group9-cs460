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

    /**
     * Main constructor that should be called when Flow meter needs to be
     * created
     *
     * @param hostName   Host name for application
     * @param portNumber Port number for application
     */
    public FlowMeter(String hostName, int portNumber) {
        display = new FMDisplay();
        display.setClient(new FMIOClient(hostName, portNumber, display));
    }

    public FMDisplay getDisplay() {
        return display;
    }

}
