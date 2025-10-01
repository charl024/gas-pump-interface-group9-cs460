/**
 * Gas station server socket, handles messages and connection between IOPort
 */
package GasServer;

import Util.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gas Station Server Socket
 */
public class GSServer implements Runnable {
    private final int portNumber;
    private final GasStation gasStation;
    private final ServerSocket serverSocket;
    private ObjectOutputStream out;
    private boolean connected = false;

    /**
     * Gas Station Server Socket
     *
     * @param portNumber Port number socket will run on
     * @param gasStation Gas Station that holds other components
     * @throws IOException
     */
    public GSServer(int portNumber, GasStation gasStation) throws IOException {
        this.portNumber = portNumber;
        this.gasStation = gasStation;
        serverSocket = new ServerSocket(portNumber);
    }

    /**
     * Thread that is ran to handle connection and messages between IOPort
     */
    @Override
    public void run() {
        System.out.println("Gas Server is running on port " + portNumber);
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                connected = true;

                out = new ObjectOutputStream(socket.getOutputStream());

                //Send initial price list to screen:
                double reg = gasStation.getDisplay().getRegularCost();
                double plus = gasStation.getDisplay().getPlusCost();
                double prem = gasStation.getDisplay().getPremiumCost();
                sendMessage(new Message("GS-INITIALPRICE-" + reg + "-" + plus + "-" + prem));
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    try {
                        Message message = (Message) in.readObject();
                        gasStation.getClient().handleMessage(message);

                    } catch (EOFException e) {
                        System.out.println("Client disconnected");
                        break;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    /**
     * Check if hose is connected or not
     *
     * @return Hose Connected
     */
    public boolean isConnected() {
        return connected;
    }
}
