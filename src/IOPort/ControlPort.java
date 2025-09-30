package IOPort;

import Util.Message;

/**
 * {@code ControlPort} is a specialized {@link IOPort} used for
 * transmitting control signals or commands. For example, sending an on/off signal
 *
 * <p>
 * This port only exposes {@link #send(Message)}, meaning it is designed
 * for outbound communication only.
 * </p>
 */
public class ControlPort extends IOPort {
    public ControlPort(int connector) {
        super(connector);
    }

    /**
     * Sends a control {@link Message} to the connected endpoint.
     *
     * @param message The control message to send.
     */
    public void send(Message message) {
        super.send(message);
    }
}
