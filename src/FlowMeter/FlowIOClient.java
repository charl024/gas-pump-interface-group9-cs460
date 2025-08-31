package FlowMeter;
//Class will handle all messages received by the connected IO port

import java.io.IOException;
import java.net.Socket;

public class FlowIOClient implements Runnable{
    private int portNumber;
    private String hostName;

    private Socket socket;
    @Override
    public void run() {
        //Update later
//        try {
//            while(true) {
//
//            }
//        } catch (IOException e) {
//            System.out.println("Error handling message");
//            e.printStackTrace();
//        }
//        catch (ClassNotFoundException e) {
//            System.out.println("Invalid message received");
//        } finally {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                System.out.println("Error closing socket");
//            }
//        }
    }

    //Handle any messages received by the string
    private void handleMessage() {

    }
}
