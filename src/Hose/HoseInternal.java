package Hose;

import IOPort.ControlPort;
import MessagePassed.Message;

public class HoseInternal {
    private final ControlPort controlPort;

//    private boolean timerOn = false;
//    private long startTime = 0;
//    private final long maxTime = 20 * 1000;

    public HoseInternal() {
        // Hose is connected to connector #6
        controlPort = new ControlPort(6);
        onDisconnect();
    }

//    public void startInternalTimer() {
//        timerOn = true;
//        startTime = System.currentTimeMillis();
//    }
//
//    public void updateTimer() {
//        if (!timerOn) {
//            return;
//        }
//
//        long currentTime = System.currentTimeMillis();
//
//        if (currentTime - startTime > maxTime) {
//            timerOn = false;
//            startTime = 0;
//            onDisconnect();
//        }
//    }

    public void close() {
        controlPort.close();
    }

    public void onConnect() {
        controlPort.send(new Message("HS-CN"));
    }

    public void onDisconnect() {
        controlPort.send(new Message("HS-DC"));
    }
}
