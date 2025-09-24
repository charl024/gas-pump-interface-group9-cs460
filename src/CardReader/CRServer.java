package CardReader;

import MessagePassed.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CRServer implements Runnable {
    private int portNumber;
    private CardReader cardReader;
    private ServerSocket serverSocket;
    private ObjectOutputStream out;

    public CRServer(int portNumber, CardReader cardReader) throws IOException {
        this.portNumber = portNumber;
        this.cardReader = cardReader;
        serverSocket = new ServerSocket(portNumber);
    }

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

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
}
