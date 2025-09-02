/**
 * Flow Meter IO Client
 * Handles all messages that need to be sent or received by the IOport
 */
package FlowMeter;

import MessagePassed.Message;
import Observer.Listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Flow Meter IO Client
 */
public class FMIOClient implements Runnable, Listener {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final int portNumber;
    private final String hostName;

    private final FMDisplay display;

    /**
     * Constructor for Flow Meter client
     * @param hostName Host name of socket
     * @param portNumber Port number of socket
     * @param display Display class that shows the GUI
     */
    public FMIOClient(String hostName, int portNumber, FMDisplay display) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.display = display;
        handleConnection();
    }

    /**
     * Handles connection that needs to be made between the Flow Meter and
     * the IOport
     */
    private void handleConnection() {
        try {
            socket = new Socket(hostName, portNumber);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException ex) {
            System.out.println("Connection failed");
            ex.printStackTrace();
            System.exit(1); //End program is connection not made
        }
    }

    /**
     * Run method that checks if any messages are being sent to the Flow
     * meter by the IOport
     */
    @Override
    public void run() {
        //Update later
        try {
            while (true) {
                Message message = (Message) in.readObject();
                if (message != null) {
                    handleMessage(message);
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling message");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid message received");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket");
            }
        }
    }

    /**
     * Handle messages that are sent by the IOport
     * @param message Message sent by the IOPort
     */
    //Handle any messages received by the string
    private void handleMessage(Message message) {
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

    @Override
    public void messageReceived(Message message) {
        //Pass message so that it can be handled
        handleMessage(message);
    }

    /**
     * Send a message back to the connected IOport
     * @param message Message being sent
     */
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("Error sending message");
            e.printStackTrace();
        }
    }


}
