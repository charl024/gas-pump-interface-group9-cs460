package CardReader;

import IOPort.CommPort;

public class CardReader {
    private CRDisplay display;
    private CRIOClient client;


    public CardReader(CommPort port) {
        client = new CRIOClient(port);
        display = new CRDisplay(client);
    }

    public CRDisplay getDisplay() {
        return display;
    }

    public void setDisplay(CRDisplay display) {
        this.display = display;
    }

    public CRIOClient getClient() {
        return client;
    }

    public void setClient(CRIOClient client) {
        this.client = client;
    }
}
