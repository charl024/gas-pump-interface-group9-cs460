package Hose;

import IOPort.PortLookupMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HoseIOServer {
    private Thread connectionThread;
    private ServerSocket serverSocket;
    private Socket hoseSocket;

    public HoseIOServer() {
        int port = PortLookupMap.PortMap(6);
        try {
            serverSocket = new ServerSocket(port);

            connectionThread = new Thread(() -> {
                try {
                    hoseSocket = serverSocket.accept();
                    ObjectOutputStream out = new ObjectOutputStream(hoseSocket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(hoseSocket.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            connectionThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
