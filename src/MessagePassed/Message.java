package MessagePassed;

import java.io.Serializable;

public class Message implements Serializable {
    private String description; //No clue what to name this, don't want same

    // name as the class
    public Message(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
