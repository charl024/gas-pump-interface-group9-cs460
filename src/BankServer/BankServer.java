package BankServer;

import IOPort.CommPort;

public class BankServer {
    private final BSDisplay display;
    private final BSIOClient client;

    public BankServer(CommPort port) {
        display = new BSDisplay();
        client = new BSIOClient(display, port);
    }

    public BSDisplay getDisplay() {
        return display;
    }

    public BSIOClient getClient() {
        return client;
    }
}
