package Hose;

import IOPort.ControlPort;
import MessagePassed.Message;

public class HoseInternal {
    // Hose needs to send back am I disconnected? am I connected?
    private ControlPort controlPort;
    private boolean connectionStatus;


    public HoseInternal() {
        // Hose is connected to connector #6
        controlPort = new ControlPort(6);
        connectionStatus = false;
    }

    private void onConnect() {
        controlPort.send(new Message("HS-CN"));
    }

    private void onDisconnect() {
        controlPort.send(new Message("HS-DC"));
    }
}
