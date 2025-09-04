package IOPort;

import MessagePassed.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The {@code IOPort} class provides a communication channel between two endpoints
 * using a single TCP port.
 *
 * <p>
 *     Once established, both endpoints can exchange {@link Message} objects using
 *     object streams. Incoming messages are stored in a single variable, which can
 *     be accessed using {@link #get()} or {@link #read()}.
 * </p>
 *
 * <p>
 *     Example usage:
 * </p>
 * <pre>{@code
 *      // On both Program A and Program B
 *      IOPort port = new IOPort(1);   // Both use the same port number
 *
 *       // Sending a message
 *      port.send(new Message("Hello"));
 *
 *      // Receiving and consuming a message
 *      Message msg = port.get();
 *
 *       // Peeking at the next message without removing it
 *       Message peek = port.read();
 * }
 * </pre>
 *
 * <p>
 *     Each IOPort runs two internal threads:
 *     One to establish the connection (as client or server).<
 *     One to continuously listen for incoming messages and stores them.
 * </p>
 */
public class IOPort {
    private final int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Message currentMessage;

    /*
        A component that connects two endpoints together by passing in a single port number.
        For example, an IoPort in Program A can connect with an IoPort in Program B by having the same port number.
     */

    /**
     * Constructor for IOPort.
     * Sets up the connection (as client or server) and starts a listening thread
     * to continuously receive messages from the other endpoint.
     *
     * @param connector The port number this IOPort instance binds to.
     */
    protected IOPort(int connector) {
        // port is obtained from mapping connector to a port #, where port directly corresponds to a device
        this.port = PortLookupMap.PortMap(connector);
        this.currentMessage = null;

        // Start a thread to establish a connection (client if possible, otherwise server).
        Thread establishConnection = getThread();

        try {
            // Wait until connection is established before proceeding.
            establishConnection.join();

            // Set up object streams for sending and receiving Message objects.
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (InterruptedException | IOException e) {
            close();
        }

        // Start a thread to continuously listen for incoming messages and place them into a variable.
        Thread listen = new Thread(() -> {
            try {
                Message message;
                while ((message = (Message) in.readObject()) != null) {
                    currentMessage = message;
                }
            } catch (IOException | ClassNotFoundException e) {
                close();
            }
        });

        listen.start();
    }

    /**
     * Starts a connection thread.
     * Tries to connect as a client first. If that fails, creates a server socket
     * and waits for a client to connect.
     *
     * @return A thread that handles the connection establishment.
     */
    private Thread getThread() {
        Thread establishConnection = new Thread(() -> {
            try {
                try {
                    // Attempt to connect as client to an existing server on localhost.
                    socket = new Socket("localhost", port);
                } catch (IOException e) {
                    // If client connection fails, act as server and wait for client.
                    serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                }

            } catch (IOException e) {
                close();
            }
        });

        establishConnection.start();
        return establishConnection;
    }

    /**
     * Safely closes all resources associated with this IOPort.
     *
     * <p>
     * This method is called whenever an error occurs
     * (e.g., connection failure, I/O exception, or stream corruption),
     * or when the connection is explicitly terminated.
     * </p>
     *
     * <p>
     * Resources closed include:
     * <ul>
     *     <li>{@link Socket} — disconnects the client/server socket.</li>
     *     <li>{@link ServerSocket} — stops listening for incoming connections.</li>
     *     <li>{@link ObjectOutputStream} — terminates the outgoing stream.</li>
     *     <li>{@link ObjectInputStream} — terminates the incoming stream.</li>
     * </ul>
     * </p>
     */
    private void close() {
        System.err.println("Connection closed!");
        try {
            if (socket != null) {
                socket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ignored) {}
    }

    /**
     * Sends a Message object to the connected endpoint.
     *
     * @param message The Message object to be sent.
     */
    protected void send(Message message) {
        if (out != null) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                close();
            }
        }

    }

    /**
     * Retrieves and returns current message, which is then set to null.
     *
     * @return The current Message.
     */
    protected Message get() {
        Message toSend = currentMessage;
        currentMessage = null;
        return toSend;
    }

    /**
     * Reads the current message.
     *
     * @return The current Message.
     */
    protected Message read() {
        return currentMessage;
    }
}