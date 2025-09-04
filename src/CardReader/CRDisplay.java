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

public class CRDisplay {
    private final CRInput input;
    private final BorderPane pane;
    private final StackPane imagePane;
    private Image tapToPay;
    private ImageView imageView;

    private final Label status;

    public CRDisplay(CRIOClient client) {
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setMinSize(350,250);
        pane.setMaxSize(350,250);
        imagePane = new StackPane();
        status = new Label("Status: ");
        status.setMinSize(50,50);
        input = new CRInput(client,this);
        createDisplay();
    }

    private void createDisplay() {
        loadImage();
        imagePane.getChildren().add(imageView);
        imagePane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        Font customFont = new Font(24);
        Label info = new Label("Tap to Pay!");
        info.setFont(customFont);
        status.setFont(customFont);

        pane.setTop(info);
        pane.setCenter(imagePane);
        pane.setBottom(status);
        BorderPane.setAlignment(info, Pos.BASELINE_CENTER);
        BorderPane.setAlignment(status, Pos.CENTER);
        BorderPane.setAlignment(imagePane, Pos.CENTER);
        pane.setOnMouseClicked(event -> {
            input.handleTap();
        });
    }

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

    private void validCard() {
        status.setText("Invalid Card, please try again");
    }
    private void invalidCard() {
        status.setText("Card was authorized!");
    }

    public BorderPane getPane() {
        return pane;
    }
}
