/**
 * Creates the user interface for Card Reader
 */
package CardReader;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;

/**
 * Card Reader display
 */
public class CRDisplay {
    private final CRInput input;
    private BorderPane pane;
    private StackPane imagePane;
    private Image tapToPay;
    private ImageView imageView;
    private Label info;
    private Pane boxTwo;
    private Pane boxThree;
    private Pane boxFour;

    /**
     * Base constructor used for demoing the GUI
     */
    public CRDisplay(CRServer server) {
        createBasePane();
        input = new CRInput(server, this);
        createDisplay();
    }


    /**
     * Creates the components of the main display
     */
    private void createDisplay() {
        loadImage();
        imagePane.getChildren().add(imageView);
        imagePane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Font customFont = new Font(18);
        info = new Label("Waiting for IOConnection");
        info.setFont(customFont);

        Pane boxOne = new Pane();
        boxOne.setMinSize(40, 40);
        boxTwo = new Pane();
        boxTwo.setMinSize(40, 40);
        boxThree = new Pane();
        boxThree.setMinSize(40, 40);
        boxFour = new Pane();
        boxFour.setMinSize(40, 40);

        boxOne.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        HBox statusBox = new HBox(boxOne, boxTwo, boxThree, boxFour);
        statusBox.setSpacing(10);
        statusBox.setAlignment(Pos.CENTER);
        finishCard();

        pane.setTop(info);
        pane.setCenter(imagePane);
        Insets insets = new Insets(10, 10, 10, 10);
        pane.setBottom(statusBox);
        BorderPane.setMargin(statusBox, insets);
        BorderPane.setAlignment(info, Pos.BASELINE_CENTER);

        BorderPane.setAlignment(imagePane, Pos.CENTER);

        imagePane.setOnMouseClicked(event -> {
            try {
                input.handleTap();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Creates the base pane holding all the needed components
     */
    private void createBasePane() {
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setMinSize(414, 200);
        pane.setMaxSize(414, 200);
        imagePane = new StackPane();
    }

    /**
     * Load the tap to pay image so that it can then be displayed
     */
    private void loadImage() {
        String fileName = "Images/WhiteTTP.png";
        File imageFile = new File(fileName);

        if (!imageFile.exists()) {
            System.out.println("File not found");
            System.exit(0);
        }
        String imagePath = "file:" + imageFile.getAbsolutePath();
        tapToPay = new Image(imagePath);

        imageView = new ImageView();
        imageView.setImage(tapToPay);
        //Adjust the size of the image
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
    }

    /**
     * Turns all boxes to be green, indicating the card reader was clicked on
     */
    public void updateStatusBox() {
        boxTwo.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        boxThree.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        boxFour.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
    }


    /**
     * Called when the user has finished pumping gas, resetting the colors of the boxes
     */
    public void finishCard() {
        boxTwo.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        boxThree.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        boxFour.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Update text box for info
     */
    public void updateInfo() {
        Platform.runLater(() -> {
            info.setText("Tap to Pay!");
        });
    }

    /**
     * Get the main borderPane that holds all components
     *
     * @return BorderPane
     */
    public BorderPane getPane() {
        return pane;
    }

    /**
     * Return class that handles user input
     *
     * @return Card reader input
     */
    public CRInput getInput() {
        return input;
    }
}
