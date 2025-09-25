package PumpAssemblyManager;

import IOPort.StatusPort;
import MessagePassed.Message;

public class PumpAssemblyManager {

    public PumpAssemblyManager() {

        StatusPort hoseStatusPort = new StatusPort(6);

        while (true) {
            Message message = hoseStatusPort.read();
            // this sleep must be here otherwise it will only receive null messages
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (message == null) continue;
            System.out.println(message);
        }

    }
}
