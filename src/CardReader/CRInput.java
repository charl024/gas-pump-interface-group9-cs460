package CardReader;

import MessagePassed.Message;

public class CRInput {
    private CRIOClient client;
    private CRDisplay display;

    public CRInput(CRIOClient client, CRDisplay display) {
        this.client = client;
        this.display = display;
    }

    public void handleTap() {
        //Send a message back to main saying that a card was tapped on screen
        //Main should indicate whether or not if the card is valid or not
        System.out.println("Clicked on");
        Message cardTap = new Message("CR-TAPPED"); //I have no idea if I like this for now
        client.sendMessage(cardTap);
    }
}
