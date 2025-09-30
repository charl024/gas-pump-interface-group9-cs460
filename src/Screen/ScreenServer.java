package Screen;

import MessagePassed.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ScreenServer implements Runnable {
    private final int portNumber;
    private final ServerSocket serverSocket;
    private ObjectOutputStream out;
    private final HandleMessage screen;

    public ScreenServer(int portNumber, HandleMessage screen) throws IOException {
        this.portNumber = portNumber;
        this.screen = screen;
        serverSocket = new ServerSocket(portNumber);
    }

    @Override
    public void run() {
        System.out.println("Screen server is running on port " + portNumber);
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Client connected");
                while (true) {
                    try {
                        Message message = (Message) in.readObject();
                        screen.handleMessage(message);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
