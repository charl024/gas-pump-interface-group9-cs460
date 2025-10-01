package Hose;

import Util.PortLookupMap;
import Util.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HoseIOServer acts as a relay server that connects exactly two clients
 * (two IOPorts). Once both clients connect, the server forwards any
 * {@link Message} sent by one client to the other.
 */
public class HoseIOServer {
    private ServerSocket serverSocket;
    private Socket clientOne;
    private Socket clientTwo;
    private ObjectInputStream inOne, inTwo;
    private ObjectOutputStream outOne, outTwo;

    /**
     * Creates a HoseIOServer bound to a port resolved from the connector number.
     *
     * @param connector a logical connector ID mapped to an actual TCP port
     */
    public HoseIOServer(int connector) {
        int port = PortLookupMap.PortMap(connector);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("HoseIOServer listening on port " + port);
    }

    /**
     * Starts the server in its own thread.
     * - Accepts incoming socket connections.
     * - Stores the first two as clientOne and clientTwo.
     * - Once two clients are connected, starts relay threads to forward messages
     * between them.
     * - Any extra clients beyond two are rejected.
     */
    public void start() {
        new Thread(() -> {
            try {
                while (true) {
                    // Block until a client connects
                    Socket client = serverSocket.accept();
                    System.out.println("New client connected!");

                    synchronized (this) {
                        if (clientOne == null) {
                            // First client slot not filled yet
                            clientOne = client;
                            outOne = new ObjectOutputStream(client.getOutputStream());
                            inOne = new ObjectInputStream(client.getInputStream());
                        } else if (clientTwo == null) {
                            // Second client slot not filled yet
                            clientTwo = client;
                            outTwo = new ObjectOutputStream(client.getOutputStream());
                            inTwo = new ObjectInputStream(client.getInputStream());

                            // Both clients are connected:
                            // start two relay threads, one in each direction.
                            new Thread(() -> relay(inOne, outTwo)).start();
                            new Thread(() -> relay(inTwo, outOne)).start();
                            System.out.println("Relay started!");
                        } else {
                            System.out.println("Rejecting extra client.");
                            client.close();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Server stopped: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Continuously reads messages from one client and writes them to the other.
     *
     * @param in  input stream from one client
     * @param out output stream to the other client
     */
    private void relay(ObjectInputStream in, ObjectOutputStream out) {
        try {
            while (true) {
                Message msg = (Message) in.readObject();

                // Write message to the other client
                synchronized (out) {
                    out.writeObject(msg);
                    out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Relay stopped: " + e.getMessage());
            close();
        }
    }

    /**
     * Safely closes both clients and the server socket.
     */
    public void close() {
        try {
            if (clientOne != null) clientOne.close();
            if (clientTwo != null) clientTwo.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ignored) {
        }
    }
}
