package IOPort;

import MessagePassed.Message;

/**
 * {@code MonitorPort} is a specialized {@link IOPort} intended for
 * monitoring messages without consuming them.
 *
 * <p>
 * It exposes {@link #send(Message)} for outgoing messages, and
 * {@link #read()} to read the current message, but does not remove it.
 * </p>
 */
public class MonitorPort extends IOPort {
    public MonitorPort(int connector) {
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
     * Reads the current message.
     *
     * @return The current Message.
     */
    public Message read() {
        return super.read();
    }
}
