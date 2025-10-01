package Util;

import IOPort.IOPort;

import java.util.List;

/**
 * The Manager interface defines the contract for all system managers.
 * Each Manager is responsible for handling a specific group of devices
 * (e.g., ScreenManager, ServerManager, PumpAssemblyManager).
 * This interface does the following stuff:
 * - Provide access to the IOPorts it manages
 * - Handle incoming messages from devices connected to those ports
 * - Send outgoing messages to the devices it controls
 */
public interface Manager {
    /**
     * Returns all IOPorts that this manager uses to communicate
     * with its devices. The MainController polls these ports.
     *
     * @return list of IOPorts owned by this manager
     */
    List<IOPort> getPorts();

    /**
     * Process an incoming message from one of this manager's devices.
     * The manager may generate new messages that need to be routed
     * to other managers by the MainController.
     *
     * @param message the incoming message
     * @return a list of messages to be forwarded to other managers
     */
    List<Message> handleMessage(Message message);

    /**
     * Send a message to one of this manager's devices.
     * Called by the MainController when routing a message
     * to this manager.
     *
     * @param message the outgoing message
     */
    List<Message> sendMessage(Message message);
}
