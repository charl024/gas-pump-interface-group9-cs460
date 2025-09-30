package Util;

import IOPort.IOPort;

import java.util.List;

public interface Manager {
    List<IOPort> getPorts();
    List<Message> handleMessage(Message message);
    void sendMessage(Message message);
}
