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
            //Start the flow meter and change the color of the cord to be green
            if (parts[1].equals("START")) {
                double costPerGal = Double.parseDouble(parts[2]);

                //Handle optional parts
                int gasFlowRate = -1;
                if (parts.length > 3) {
                    gasFlowRate = Integer.parseInt(parts[3]);
                }

                int totalGas = -1;
                if (parts.length > 4) {
                    totalGas = Integer.parseInt(parts[4]);
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
                display.startPump();


            } else if (parts[1].equals("PAUSE")) {
                display.pauseTimer();
            } else if (parts[1].equals("RESUME")) {
                display.startTimer();
            } else if (parts[1].equals("TOTAL")) {
                //Total should only be requested if the pump has stopped
                if (!display.isTimerRunning()) {
                    display.sendGasTotals();
                }
            }

        }
    }

}
