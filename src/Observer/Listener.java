package Observer;

import MessagePassed.Message;

public interface Listener {
    void messageReceived(Message message);
}
