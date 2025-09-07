package GasServer;

import IOPort.CommPort;

public class GasStation {
    private final GSDisplay display;
    private final GSIOClient client;

    public GasStation(CommPort port) {
        display = new GSDisplay();
        client = new GSIOClient(display, port);

    }

    public GSDisplay getDisplay() {
        return display;
    }

    public GSIOClient getClient() {
        return client;
    }
}
