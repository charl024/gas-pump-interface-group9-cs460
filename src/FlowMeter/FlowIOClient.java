package FlowMeter;
//Class will handle all messages received by the connected IO port

import MessagePassed.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FlowIOClient implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int portNumber;
    private String hostName;


    @Override
    public void run() {
        //Update later
        try {
            while (true) {
                Message message = (Message) in.readObject();
                if (message != null) {
                    handleMessage();
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
    private void handleMessage() {

    }
}
