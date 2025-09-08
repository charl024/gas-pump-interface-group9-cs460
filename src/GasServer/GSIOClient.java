/**
 * Gas Station Server IOPort Client
 */
package GasServer;

import IOPort.CommPort;
import MessagePassed.Message;

/**
 * Gas Station Server IOPort Client
 */
public class GSIOClient {
    private final GSDisplay display;
    private final CommPort port;

    /**
     * Constructor for Gas Station IOPort client
     *
     * @param display Display that handles the GUI
     * @param port    Connect IOPort
     */
    public GSIOClient(GSDisplay display, CommPort port) {
        this.display = display;
        this.port = port;
    }

    /**
     * Handles messaged received from the IOPort
     *
     * @param message Message receieved
     */
    public void handleMessage(Message message) {
        //Types of messages that can be received:
        //GS-NEWTOTAL-10.55
        //GS-UPDATEGAS-3.55-3.80-4.00
        //GS-GASINFO -> Sends Message -> GS-GASINFODONE-3.20-3.60-4.10

        String receivedMessage = message.getDescription();
        String[] parts = receivedMessage.split("-");
        if (!(parts[0].equals("GS"))) {
            Message invalidMessage = new Message("GS-Invalid");
            port.send(invalidMessage);
        } else {
            String gasStationRequestType = parts[1];
            //If we receive a customers total, then just update current total
            switch (gasStationRequestType) {
                case "NEWTOTAL" -> {
                    double total = Double.parseDouble(parts[2]);
                    display.setTotal(display.getTotal() + total);
                    display.updateTotal();
                }
                case "UPDATEGAS" -> {
                    //Going to assume that all prices will change, or if one gets
                    // updated, the other prices need to be included
                    double newRegularPrice = Double.parseDouble(parts[2]);
                    double newPlusPrice = Double.parseDouble(parts[3]);
                    double newPremiumPrice = Double.parseDouble(parts[4]);
                    display.setRegularCost(newRegularPrice);
                    display.setPlusCost(newPlusPrice);
                    display.setPremiumCost(newPremiumPrice);
                    display.updateGasPrices();
                }
                case "GASINFO" -> {
                    //Screen is likely the only one requesting information about
                    // the gas information, wouldn't make sense for another
                    // device to request this information since this will be
                    // giving the gas costs for all three types
                    double curRegularPrice = display.getRegularCost();
                    double curPlusPrice = display.getPlusCost();
                    double curPremiumPrice = display.getPremiumCost();
                    Message gasInfo = new Message("GS-GASINFODONE-" + curRegularPrice + "-" + curPlusPrice + "-" + curPremiumPrice);
                    port.send(gasInfo);
                }
            }
        }
    }
}
