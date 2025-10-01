/**
 * Message class, used in order to send messages between devices
 */
package Util;

import java.io.Serializable;

/**
 * Message Class
 */
public class Message implements Serializable {
    private String description; //No clue what to name this, don't want same

    /**
     * Empty Constructor
     */
    public Message() {

    }

    /**
     * Message constructor
     *
     * @param description Description for message
     */
    public Message(String description) {
        this.description = description;
    }

    /**
     * Add to current description
     *
     * @param description Description being addeded to current one
     */
    public void addToDescription(String description) {
        this.description += description;
    }

    /**
     * Get current description
     *
     * @return Current description
     */
    public String getDescription() {
        return description;
    }


    /**
     * Change device, ie FM -> SC
     *
     * @param newDevice New device
     */
    public void changeDevice(String newDevice) {
        description = newDevice.substring(0, 2) + description.substring(2);
    }

    /**
     * To string to print what message was sent
     *
     * @return Description
     */
    @Override
    public String toString() {
        return description;
    }
}
