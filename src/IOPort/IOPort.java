package IOPort;

import MessagePassed.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@code IOPort} class provides a communication channel between two endpoints
 * using a single TCP port.
 *
 * <p>
 *     Once established, both endpoints can exchange {@link Message} objects using
 *     object streams. Incoming messages are stored in a thread-safe queue, which can
 *     be accessed using {@link #get()} or {@link #read()}.
 * </p>
 *
 * <p>
 *     Example usage:
 * </p>
 * <pre>{@code
 *      // On both Program A and Program B
 *      IOPort port = new IOPort(5050);   // Both use the same port number
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
 *     One to continuously listen for incoming messages and enqueue them.
 * </p>
 */
public class IOPort {
    private final int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final BlockingQueue<Message> messageQueue;

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
        this.port = connector;
        this.messageQueue = new LinkedBlockingQueue<>();

        // Start a thread to establish a connection (client if possible, otherwise server).
        Thread establishConnection = getThread();

        try {
            // Wait until connection is established before proceeding.
            establishConnection.join();

            // Set up object streams for sending and receiving Message objects.
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        // Start a thread to continuously listen for incoming messages and place them into the message queue.
        Thread listen = new Thread(() -> {
            try {
                Message message;
                while ((message = (Message) in.readObject()) != null) {
                    messageQueue.put(message);
                }
            } catch (InterruptedException | IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
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
                throw new RuntimeException(e);
            }
        });

        establishConnection.start();
        return establishConnection;
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
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Retrieves and removes the next available Message from the queue.
     * This call will block until a message is available.
     *
     * @return The next Message from the queue.
     */
    protected Message get() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads (peeks) the next available Message without removing it from the queue.
     * Achieved by taking the message out and immediately putting it back in.
     * This call will block until a message is available.
     *
     * @return The next Message from the queue.
     */
    protected Message read() {
        try {
            Message message = messageQueue.take();
            messageQueue.put(message);
            return message;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}