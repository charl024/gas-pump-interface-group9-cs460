package IOPort;

import MessagePassed.Message;

/**
 * {@code CommPort} is a specialized {@link IOPort} used for
 * bidirectional communication between components.
 *
 * <p>
 * It provides both {@link #send(Message)} for transmitting messages
 * and {@link #get()} for returning the most recent message.
 * </p>
 */
public class CommPort extends IOPort {
    public CommPort(int connector) {
        super(connector);
    }

    /**
     * Sends a {@link Message} to the connected endpoint.
     *
     * @param message The message to send.
     */
    public void send(Message message) {
        super.send(message);
    }

    /**
     * Retrieves and returns current message, which is then set to null.
     *
     * @return The current Message.
     */
    public Message get() {
        return super.get();
    }
}
