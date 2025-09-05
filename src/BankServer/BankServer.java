package BankServer;

import IOPort.CommPort;

public class BankServer {
    private CommPort port;
    private BSDisplay display;
    private BSIOClient client;

    public BankServer(CommPort port) {
        this.port = port;
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
