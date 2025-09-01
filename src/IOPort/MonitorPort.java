package IOPort;

import MessagePassed.Message;

/**
 * {@code MonitorPort} is a specialized {@link IOPort} intended for
 * monitoring messages without consuming them.
 *
 * <p>
 * It exposes {@link #send(Message)} for outgoing messages, and
 * {@link #read()} to read the most recent message in the queue (doesn't remove the message from queue).
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
     * Reads the next available {@link Message} without consuming it.
     *
     * @return The next message in the queue.
     */
    public Message read() {
        return super.read();
    }
}
