package IOPort;

import MessagePassed.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IOPort {
    int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final BlockingQueue<Message> messageQueue;

    /*
        A component that connects the main controller to other devices
        For example, we can "attach" the input of Controller to the output of Screen, or another device (and vice versa)
        More abstract, we can connect a Controller object with a Device object (Screen can be a child class of this)

        Just a placeholder for future stuff, change later to fit our needs
     */

    public IOPort(int connector) {
        // port is obtained from mapping connector to a port #, where port directly corresponds to a device
        this.port = connector;
        this.messageQueue = new LinkedBlockingQueue<>();

        Thread establishConnection = getThread();

        try {
            establishConnection.join();
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

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

    private Thread getThread() {
        Thread establishConnection = new Thread(() -> {
            try {
                try {
                    // try this first, if success connect as a client
                    socket = new Socket("localhost", port);
                } catch (IOException e) {
                    // if the above fails, connect as server
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

    public void send(Message message) {
        if (out != null) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Message get() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Message read() {
        try {
            Message message = messageQueue.take();
            messageQueue.put(message);
            return message;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}