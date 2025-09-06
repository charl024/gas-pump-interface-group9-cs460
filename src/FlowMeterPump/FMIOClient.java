/**
 * Flow Meter IO Client
 * Handles all messages that need to be sent or received by the IOport
 */
package FlowMeterPump;

import IOPort.CommPort;
import MessagePassed.Message;


/**
 * Flow Meter IO Client
 */
public class FMIOClient {


    private final FMDisplay display;
    private CommPort port;

    /**
     * Constructor for Flow Meter client
     *
     * @param display Display class that shows the GUI
     */
    public FMIOClient(FMDisplay display) {
        this.display = display;
    }


    /**
     * Handle messages that are sent by the IOport
     *
     * @param message Message sent by the IOPort
     */
    public void handleMessage(Message message) {
        //The string should give three types of information:
        //Gas cost (based on octane they selected)
        //Maybe flow rate? Not sure if that makes sense but ill handle it


        //Not sure if flow meter will need to any other types of messages, other then here is the price, start pumping

        //Example message input: FM-2.86-10-15
        //First part tells what device it is for: FM = Flowmeter
        //Second part tells us how much the gas is going to be: 2.86 per gallon
        //Third part tells us how fast the gas is being pumped (OPTIONAL): 10 gallons per minute
        //Fourth part tells us how much gas is needed (OPTIONAL): 15 gallons total

        String messageRE = message.getDescription();
        String[] parts = messageRE.split("-");


        String deviceType = parts[0];
        if (!(deviceType.equals("FM"))) {
            //Wrong messaged received if we get here
            Message invalidMessage = new Message("FM-Invalid");
            sendMessage(invalidMessage);
        } else {

            double costPerGal = Double.parseDouble(parts[1]);

            //Handle optional parts
            int gasFlowRate = -1;
            if (parts.length > 2) {
                gasFlowRate = Integer.parseInt(parts[2]);
            }

            int totalGas = -1;
            if (parts.length > 3) {
                totalGas = Integer.parseInt(parts[3]);
            }

            //After getting the required information, start the timer to have the flow start
            display.setGasRate(costPerGal);
            if (!(gasFlowRate == -1)) {
                display.setVolRate(gasFlowRate);
            } else {
                display.setVolRate(10); //default should just be 10 gallons be per minute
            }
            if (!(totalGas == -1)) {
                display.setTotalVolume(totalGas);
            } else {
                display.setTotalVolume(15); //Not sure if there should even be a default volume size
            }

            //After all rates are set, we can now start the timer
            display.setTimerRunning(true);
            display.startGasTimer();

        }
    }

//    /**
//     * Notify the device that a messaged has been passed
//     *
//     * @param message Message received
//     */
//    @Override
//    public void messageReceived(Message message) {
//        //Pass message so that it can be handled
//        handleMessage(message);
//        //Not sure if I like this, seems kind of pointless if I just end up
//        // only calling another method
//    }

    /**
     * Send a message back to the connected IOport
     *
     * @param message Message being sent
     */
    public void sendMessage(Message message) {
        port.send(message);
    }

    /**
     * Set IOPort for Device
     *
     * @param port Connected device
     */
    public void setPort(CommPort port) {
        this.port = port;
    }
}
