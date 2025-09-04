package CardReader;

import IOPort.CommPort;
import MessagePassed.Message;
import Observer.Listener;

public class CRIOClient implements Listener {
    private CommPort port;

    public CRIOClient(CommPort port) {
        this.port = port;
    }

    private void handleMessage(Message message) {
        //Two possible values that can happen: card is valid, or card isn't
    }
    public void sendMessage(Message message) {
        port.send(message);
    }

    @Override
    public void messageReceived(Message message) {
        handleMessage(message);
    }


}
