package IOPort;

import java.util.Map;

/**
 * {@code PortLookupMap} provides a fixed mapping between connector integer values
 * and their corresponding port numbers.
 *
 * <p>
 * This utility class is used to connect device interfaces to a TCP port using simple connector values instead of
 * direct port numbers.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 *   int connectorId = 2;
 *   int portNumber = PortLookupMap.PortMap(connectorId); // returns 32
 * }</pre>
 */
public class PortLookupMap {
    /**
     * A static, immutable mapping between connector IDs and port numbers.
     * <p>
     * Keys = connector identifiers (e.g., 1, 2, 3, ...).
     * Values = corresponding port numbers (e.g., 31, 32, 33, ...).
     * </p>
     */
    private static final Map<Integer, Integer> PortMap = Map.of(
            1, 5050,
            2, 5051,
            3, 5052,
            4, 5053,
            5, 5054,
            6, 5055
    );

    /**
     * Looks up the port number associated with a given connector ID.
     *
     * @param connector The connector identifier (must exist in the map).
     * @return The corresponding port number.
     * @throws NullPointerException if the connector is not found in the map.
     */
    public static int PortMap(int connector) {
        int port;
        try {
            port = PortMap.get(connector);
            return port;
        } catch (NullPointerException e) {
            throw new NullPointerException("Unable to find port for connector " + connector);
        }
    }
}
