/**
 * Flow Meter socket server handles connection and messages between IOPort
 */
package FlowMeterPump;

import MessagePassed.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Flow Meter Server Socket
 */
public class FMServer implements Runnable {
    private final int portNumber;
    private final ServerSocket serverSocket;
    private ObjectOutputStream out;
    private final FlowMeter flowMeter;

    /**
     * Flow Meter Server Constructor
     *
     * @param portNumber Port number socket runs on
     * @param flowMeter  Flow meter that connects with various needed parts
     * @throws IOException
     */
    public FMServer(int portNumber, FlowMeter flowMeter) throws IOException {
        this.portNumber = portNumber;
        this.flowMeter = flowMeter;
        serverSocket = new ServerSocket(portNumber);
    }

    /**
     * Thread that is ran to handle connection and messages between client
     */
    @Override
    public void run() {
        System.out.println("Flow meter pump is running on port " + portNumber);
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in =
                        new ObjectInputStream(socket.getInputStream());

                while (true) {
                    try {
                        Message message = (Message) in.readObject();
                        System.out.println("Message received");
                        flowMeter.getClient().handleMessage(message);

                    } catch (EOFException e) {
                        System.out.println("Client disconnected");
                        break;
                    }
                }
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send message to client
     *
     * @param message Message
     */
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
