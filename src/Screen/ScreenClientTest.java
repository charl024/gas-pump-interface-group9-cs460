package Screen;

import IOPort.PortLookupMap;
import MessagePassed.Message;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ScreenClientTest {
    public static void main(String[] args) throws Exception {
        // Connect to the same port ScreenServer is using
        Socket socket = new Socket("localhost", PortLookupMap.PortMap(6));
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // Send INITIALPRICE message
        Message msg = new Message("SC-INITIALPRICE-3.59-3.89-4.19");
        out.writeObject(msg);
        out.flush();
        System.out.println("Sent INITIALPRICE message");

        // Wait a bit, then send VALIDCARD
        Thread.sleep(3000);
        out.writeObject(new Message("SC-VALIDCARD"));
        out.flush();
        System.out.println("Sent VALIDCARD message");

        // Wait and then send CHANGEPRICES
        Thread.sleep(7000);
        out.writeObject(new Message("SC-CHANGEPRICES-3.79-4.09-4.39"));
        out.flush();
        System.out.println("Sent CHANGEPRICES message");

        // Listen for server responses
        while (true) {
            Message response = (Message) in.readObject();
            System.out.println("Received from server: " + response.getDescription());
        }
    }
}