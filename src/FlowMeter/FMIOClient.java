package FlowMeter;
//Class will handle all messages received by the connected IO port

import MessagePassed.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FMIOClient implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int portNumber;
    private String hostName;

    private FMDisplay display;

    public FMIOClient(String hostName, int portNumber, FMDisplay display) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.display = display;
        handleConnection();
    }
    private void handleConnection(){

    }

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


    }

    public void sendMessage(Message message) {

    }
}
