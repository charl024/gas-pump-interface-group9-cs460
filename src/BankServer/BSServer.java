/**
 * Server that handles IOPort connections
 */
package BankServer;

import MessagePassed.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Bank server socket server
 */
public class BSServer implements Runnable {
    private final int portNumber;
    private final BankServer bankServer;
    private final ServerSocket serverSocket;

    /**
     * Constructor for Bank Server Socket
     *
     * @param portNumber Port number socket is being created on
     * @param bankServer Main bank server that has most components
     * @throws IOException
     */
    public BSServer(int portNumber, BankServer bankServer) throws IOException {
        this.portNumber = portNumber;
        this.bankServer = bankServer;
        serverSocket = new ServerSocket(portNumber);
    }

    /**
     * Thread that is started, will continuously handle any messages received
     * by its clients
     */
    @Override
    public void run() {
        System.out.println("Bank Server is running on port " + portNumber);
        bankServer.getDisplay().waitingConnection();
        try {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");
            bankServer.getDisplay().resetCardInfo();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    System.out.println("Message received");

                    Message bankResponse = bankServer.getClient().handleMessage(message);
                    out.writeObject(bankResponse);
                    out.flush();
                } catch (EOFException eof) {
                    System.out.println("Client disconnected.");
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
}
