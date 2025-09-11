package Hose;

import IOPort.ControlPort;
import MessagePassed.Message;

public class HoseInternal {
    private final int hoseConnector = 6;
    private ControlPort controlPort;
    private boolean portConnected;

    public HoseInternal() {
        // Hose is connected to connector #6
        Thread waitConnection = new Thread(() -> {
            controlPort = new ControlPort(hoseConnector);
            portConnected = true;
            System.out.println("You have connected to Main");
        });
        System.out.println("Launching wait thread.");
        waitConnection.start();

        onDisconnect();
    }

    public void close() {
        if (portConnected) {
            controlPort.close();
        }
    }

    public void onConnect() {
        if (portConnected) {
            controlPort.send(new Message("HS-CN"));
        }
    }

    public void onDisconnect() {
        if (portConnected) {
            controlPort.send(new Message("HS-DC"));

        }
    }
}
