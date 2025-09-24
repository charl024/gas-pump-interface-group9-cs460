package FlowMeterPump;

import MessagePassed.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FMLServer implements Runnable {
    private int portNumber;
    private ServerSocket serverSocket;
    private ObjectOutputStream out;
    private FlowMeter flowMeter;


    public FMLServer(int portNumber, FlowMeter flowMeter) throws IOException {
        this.portNumber = portNumber;
        this.flowMeter = flowMeter;
        serverSocket = new ServerSocket(portNumber);
    }

    @Override
    public void run() {
        System.out.println("Flow meter pump is running on port " + portNumber);
        try {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in =
                    new ObjectInputStream(socket.getInputStream());

            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    System.out.println("Message received");
                    flowMeter.getClient().handleMessage(message);

                } catch (EOFException e) {
                    System.out.println("Client disconnected");
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
