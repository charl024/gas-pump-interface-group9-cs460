package Hose;

import IOPort.ControlPort;
import Util.Message;

public class HoseIOClient {
    private ControlPort controlPort;
    private boolean portConnected;

    public HoseIOClient(int connector) {
        Thread waitConnection = new Thread(() -> {
            controlPort = new ControlPort(connector);
            portConnected = true;
            System.out.println("You have connected.");
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
