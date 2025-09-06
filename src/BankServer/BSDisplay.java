package BankServer;

import MessagePassed.Message;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class BSDisplay {
    private final BorderPane pane;
    private VBox vbox;
    private final Label cardInfo;
    private final Label cardValidation;
    private final PauseTransition delay;
    private final Text accountNum;
    private final Text status;


    public BSDisplay() {
        pane = new BorderPane();
        vbox = new VBox();
        cardInfo = new Label();
        cardValidation = new Label();

        cardInfo.setAlignment(Pos.CENTER);

        cardValidation.setAlignment(Pos.CENTER);
        accountNum = new Text("Account Number: ");
        accountNum.setStyle("-fx-font-weight: bold");
        status = new Text("Status: ");
        status.setStyle("-fx-font-weight: bold");

        vbox.getChildren().addAll(cardInfo, cardValidation);
        vbox.setAlignment(Pos.CENTER);
        delay = new PauseTransition(Duration.seconds(15)); //set the duration
        // for when the text should go back to default format
        delay.setOnFinished(event -> resetCardInfo());

        pane.setCenter(vbox);
        resetCardInfo();
        pane.setMinSize(250, 250);
    }

    private void resetCardInfo() {
        cardInfo.setGraphic(accountNum);
        cardValidation.setGraphic(status);
    }

    public void updateValidCard(Message message) {
        delay.playFromStart(); //If we update the info, reset timer
        String description = message.getDescription();
        String[] parts = description.split("-");
        String accountNumString = parts[1];

        Text messageAccountNum = new Text(accountNumString);
        TextFlow textFlow = new TextFlow(accountNum, messageAccountNum);
        cardInfo.setGraphic(textFlow);

        Text valid = new Text("Valid  ");
        TextFlow validation = new TextFlow(status, valid);
        cardValidation.setGraphic(validation);
    }

    public void updateInvalidCard(Message message) {
        delay.playFromStart(); //If we update the info, reset timer
        String description = message.getDescription();
        String[] parts = description.split("-");
        String accountNum = parts[1];

        Text messageAccountNum = new Text(accountNum);
        TextFlow textFlow = new TextFlow(messageAccountNum);
        cardInfo.setGraphic(textFlow);

        Text invalid = new Text("Invalid");
        TextFlow validation = new TextFlow(status, invalid);
        cardValidation.setGraphic(validation);
    }

    public BorderPane getPane() {
        return pane;
    }
}
