package MessagePassed;

import java.io.Serializable;

public class Message implements Serializable {
    private String description; //No clue what to name this, don't want same

    public Message() {

    }

    // name as the class
    public Message(String description) {
        this.description = description;
    }

    public void addToDescription(String description) {
        this.description += description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void changeDevice(String newDevice) {
        description = newDevice.substring(0, 2) + description.substring(2);
    }
}
