/**
 * Bank server display, handles showing information for the bank server
 */
package BankServer;

import Util.Message;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

/**
 * Bank Server Display
 */
public class BSDisplay {
    private final BorderPane pane;
    private VBox vbox;
    private final Label cardInfo;
    private final Label cardValidation;
    private final PauseTransition delay;
    private final Text accountNum;
    private final Text status;

    /**
     * Bank Server Display constructor, creates all the needed panes and text
     */
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

    /**
     * Reset Card information, removes the current card number and status of it
     */
    public void resetCardInfo() {
        Platform.runLater(() -> {
            cardInfo.setGraphic(accountNum);
            cardValidation.setGraphic(status);
        });
    }

    /**
     * Called to indicate that the Bank server is waiting for a IOPort
     * connection
     */
    public void waitingConnection() {
        cardInfo.setGraphic(new Text("Waiting for IOConnection"));
        cardValidation.setGraphic(new Text("Waiting for IOConnection"));
    }

    /**
     * Update graphic to indicate that the given card is valid
     *
     * @param message Message containing the account information
     */
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

    /**
     * Update graphic to indicate that the given card is invalid
     *
     * @param message Message containing the account information
     */
    public void updateInvalidCard(Message message) {
        delay.playFromStart(); //If we update the info, reset timer
        String description = message.getDescription();
        String[] parts = description.split("-");
        String accountNumString = parts[1];

        Text messageAccountNum = new Text(accountNumString);
        TextFlow textFlow = new TextFlow(accountNum, messageAccountNum);
        cardInfo.setGraphic(textFlow);

        Text invalid = new Text("Invalid");
        TextFlow validation = new TextFlow(status, invalid);
        cardValidation.setGraphic(validation);
    }

    /**
     * Get BorderPane that combines all the other panes
     *
     * @return Main Pane
     */
    public BorderPane getPane() {
        return pane;
    }
}
