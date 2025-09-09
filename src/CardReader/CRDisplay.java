/**
 * Creates the user interface for Card Reader
 */
package CardReader;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

/**
 * Card Reader display
 */
public class CRDisplay {
    private final CRInput input;
    private final BorderPane pane;
    private final StackPane imagePane;
    private Image tapToPay;
    private ImageView imageView;
    private Pane boxTwo;
    private Pane boxThree;
    private Pane boxFour;


    /**
     * Card Reader Display constructor
     *
     * @param client Client that handles IOPort messages
     */
    public CRDisplay(CRIOClient client) {
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setMinSize(350, 250);
        pane.setMaxSize(350, 250);
        imagePane = new StackPane();



        input = new CRInput(client, this);
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
        Label info = new Label("Tap to Pay!");
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
        pane.setOnMouseClicked(event -> {
            input.handleTap();
        });
    }

    /**
     * Load the tap to pay image so that it can then be displayed
     */
    private void loadImage() {
//        String fileName = "Images/BlackTTP.png";
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
     * Turns all boxes to be green, indicating that the card was valid
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
     * Get the main borderPane that holds all components
     *
     * @return BorderPane
     */
    public BorderPane getPane() {
        return pane;
    }
}
