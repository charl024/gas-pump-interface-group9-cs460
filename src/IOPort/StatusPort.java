package IOPort;

import MessagePassed.Message;

/**
 * {@code StatusPort} is a specialized {@link IOPort} designed for
 * observing the latest status updates.
 *
 * <p>
 * It exposes only {@link #read()}, which allows for reading the most recent message without removing it from queue.
 * </p>
 */
public class StatusPort extends IOPort {
    public StatusPort(int connector) {
        super(connector);
    }

    /**
     * Reads the next available {@link Message} without consuming it.
     * Blocks if no message is currently available.
     *
     * @return The next status message.
     */
    public Message read() {
        return super.read();
    }
}
