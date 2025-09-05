package BankServer;

import MessagePassed.Message;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class BSDisplay {
    private BorderPane pane;
    private Label cardInfo;
    private PauseTransition delay;

    public BSDisplay() {
        pane = new BorderPane();
        cardInfo = new Label();
        delay = new PauseTransition(Duration.seconds(30)); //set the duration
        // for when the text should go back to default format
        delay.setOnFinished(event -> resetCardInfo());

        pane.setCenter(cardInfo);
        resetCardInfo();
        cardInfo.setMinSize(250, 250);
    }

    private void resetCardInfo() {
        cardInfo.setText("Account Number: \nStatus: ");
    }

    public void updateValidCard(Message message) {
        //TESTING THIS WILL FIX SOON
//        Text accountNumText = new Text("Account Number: ");
//        Text accountNum = new Text("World");
//        accountNumText.setStyle("-fx-font-weight: bold;");
//
//        TextFlow textFlow = new TextFlow(accountNumText, accountNum);
//
//        Label label = new Label();
//        label.setGraphic(textFlow);


        delay.playFromStart(); //If we update the info, reset timer
        String description = message.getDescription();
        String[] parts = description.split("-");
        String accountNum = parts[1];
        cardInfo.setText("Account Number: " + accountNum + "\nStatus: Valid");
    }

    public void updateInvalidCard(Message message) {
        delay.playFromStart(); //If we update the info, reset timer
        String description = message.getDescription();
        String[] parts = description.split("-");
        String accountNum = parts[1];

        cardInfo.setText("Account Number: " + accountNum + "\nStatus: Invalid");
    }

    public BorderPane getPane() {
        return pane;
    }
}
