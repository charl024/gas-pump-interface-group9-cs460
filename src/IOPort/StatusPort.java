package IOPort;

import MessagePassed.Message;

/**
 * {@code StatusPort} is a specialized {@link IOPort} designed for
 * observing the latest status updates.
 *
 * <p>
 * It exposes only {@link #read()}, which allows for reading the current message without removing it.
 * </p>
 */
public class StatusPort extends IOPort {
    public StatusPort(int connector) {
        super(connector);
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
