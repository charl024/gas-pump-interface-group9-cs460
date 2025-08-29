package IOPort;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IOPort {
    private ServerSocket serverSocket;
    private Socket peerSocket;
    private Socket deviceSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final BlockingQueue<String> messageQueue;

    /*
        A component that connects the main controller to other devices
        For example, we can "attach" the input of Controller to the output of Screen, or another device (and vice versa)
        More abstract, we can connect a Controller object with a Device object (Screen can be a child class of this)

        Just a placeholder for future stuff, change later to fit our needs
     */

    public IOPort(int connector) {

        // port is obtained from mapping connector to a port #, where port directly corresponds to a device
        int device_port = connector;

        try {
            System.out.println("Connecting to port " + device_port);
            serverSocket = new ServerSocket(device_port);

            Thread acceptThread = new Thread(() -> {
                try {
                    peerSocket = serverSocket.accept();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            acceptThread.start();

            deviceSocket = new Socket("localhost", device_port);

            acceptThread.join();

            in = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
            out = new PrintWriter(deviceSocket.getOutputStream(), true);
            messageQueue =  new LinkedBlockingQueue<>(1);
            System.out.println("Socket stream connected");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread listener = new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    synchronized (messageQueue) {
                        messageQueue.put(message);
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        listener.start();
        System.out.println("IO Port listening for messages");
    }

    public void send(String message) {
        out.println(message);
    }

    public String get() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String read() {
        try {
            String message = messageQueue.take();
            messageQueue.put(message);
            return message;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}