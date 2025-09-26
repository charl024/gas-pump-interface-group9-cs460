/**
 * Card reader Socket server, handles connections/messages from IOPort
 */
package CardReader;

import MessagePassed.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Card reader socket server
 */
public class CRServer implements Runnable {
    private final int portNumber;
    private final CardReader cardReader;
    private final ServerSocket serverSocket;
    private ObjectOutputStream out;

    /**
     * Constructor for Card Reader Socket server
     * @param portNumber Port number socket is running on
     * @param cardReader Card reader that holds all the components
     * @throws IOException
     */
    public CRServer(int portNumber, CardReader cardReader) throws IOException {
        this.portNumber = portNumber;
        this.cardReader = cardReader;
        serverSocket = new ServerSocket(portNumber);
    }

    /**
     * Thread that is ran to handle connection/messages between its clients
     */
    @Override
    public void run() {
        System.out.println("Card reader is running on port " + portNumber);
        try {
            Socket socket = serverSocket.accept();

            cardReader.getDisplay().getInput().connected();
            cardReader.getDisplay().updateInfo();

            System.out.println("Client connected");
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send message back to connected client
     * @param message Message being sent
     * @throws IOException
     */
    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
}
