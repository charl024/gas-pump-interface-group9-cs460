package CardReader;

import IOPort.CommPort;

public class CardReader {
    private final CRDisplay display;
    private CRIOClient client;


    public CardReader(CommPort port) {
        client = new CRIOClient(port);
        display = new CRDisplay(client);
        client.setDisplay(display);
    }

    public CRDisplay getDisplay() {
        return display;
    }

}
