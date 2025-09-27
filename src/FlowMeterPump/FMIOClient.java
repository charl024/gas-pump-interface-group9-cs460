/**
 * Flow Meter IO Client
 * Handles all messages that need to be sent or received by the IOport
 */
package FlowMeterPump;

import MessagePassed.Message;


/**
 * Flow Meter IO Client
 */
public class FMIOClient {

    private final FMDisplay display;
    private final FMLServer server;

    /**
     * Constructor for Flow Meter client
     *
     * @param display Display class that shows the GUI
     */
    public FMIOClient(FMDisplay display, FMLServer server) {
        this.display = display;
        this.server = server;
    }


    /**
     * Handle messages that are sent by the IOport
     *
     * @param message Message sent by the IOPort
     */
    public void handleMessage(Message message) {
        //Example message input: FM-START-2.86-10-15
        //First part tells what device it is for: FM = Flow meter
        //Second part tells us how much the gas is going to be: 2.86 per gallon
        //Third part tells us how fast the gas is being pumped (OPTIONAL): 10 gallons per minute
        //Fourth part tells us how much gas is needed (OPTIONAL): 15 gallons total

        String messageRE = message.getDescription();
        String[] parts = messageRE.split("-");


        String deviceType = parts[0];
        if (!(deviceType.equals("FM"))) {
            //Wrong messaged received if we get here
            Message invalidMessage = new Message("FM-Invalid");
            server.sendMessage(invalidMessage);
        } else {
            String event = parts[1];
            //Start the flow meter and change the color of the cord to be green
            if (event.equals("START")) {
                display.pickRandomSize();
                display.setTimerRunning(true);
                display.startGasTimer();
                display.startPump();

            } else if (event.equals("GASSELECTION")) {
                double costPerGal = Double.parseDouble(parts[2]);
                display.setGasRate(costPerGal);
            } else if (event.equals("PAUSE")) {
                display.pauseTimer();
            } else if (event.equals("RESUME")) {
                display.startTimer();
            } else if (event.equals("TOTAL")) {
                //Total should only be requested if the pump has stopped
                if (!display.isTimerRunning()) {
                    display.sendGasTotals();
                }
            }

        }
    }

}
