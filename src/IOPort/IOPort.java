package IOPort;

import MessagePassed.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The {@code IOPort} class provides a communication channel between two endpoints
 * using a single TCP port. Acts as a client.
 *
 * <p>
 * Once established, both endpoints can exchange {@link Message} objects using
 * object streams. Incoming messages are stored in a single variable, which can
 * be accessed using {@link #get()} or {@link #read()}.
 * </p>
 *
 * <p>
 * Each IOPort runs an internal thread to continuously listen for incoming messages and store them.
 * </p>
 */
public class IOPort {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Message currentMessage;
    private Thread listener;

    /*
        A component that connects two endpoints together by passing in a single port number.
        For example, an IoPort in Program A can connect with an IoPort in Program B by having the same port number.
     */

    /**
     * Constructor for IOPort.
     * Sets up the connection (as client) and starts a listening thread
     * to continuously receive messages from the other endpoint.
     *
     * @param connector The port number this IOPort instance binds to.
     */
    protected IOPort(int connector) {
        // port is obtained from mapping connector to a port #, where port directly corresponds to a device
        int port = PortLookupMap.PortMap(connector);
        this.currentMessage = null;

        try {
            // create a socket on port #
            socket = new Socket("localhost", port);

            // Set up object streams for sending and receiving Message objects.
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            close();
        }

        // Start a thread to continuously listen for incoming messages and place them into a variable.
        listener = new Thread(() -> {
            try {
                Message message;
                while ((message = (Message) in.readObject()) != null) {
                    currentMessage = message;
                }
            } catch (IOException | ClassNotFoundException e) {
                close();
            }
        });

        listener.setDaemon(true);
        listener.start();
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
    public void close() {
        System.out.println("Connection closed!");
        try {
            if (listener != null) {
                listener.interrupt();
            }
            if (socket != null) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ignored) {
        }
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