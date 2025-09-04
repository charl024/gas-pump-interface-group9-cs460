package Screen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GasSelectionScreen {

    public enum GasType {
        REGULAR,
        PLUS,
        PREMIUM
    }

    public Scene createScene(Stage stage, Runnable showReceiptScreen) {
        HBox hbox = new HBox(30); // space between buttons
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #0f0f0f, #1a1a1a);" +
                        "-fx-padding: 40;"
        );

        String baseStyle =
                "-fx-background-radius: 15;" +
                        "-fx-padding: 20 50;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,255,255,0.8), 15, 0, 0, 0);" +
                        "-fx-cursor: hand;";

        Button regularBtn = createGasButton("Regular", baseStyle + "-fx-background-color: linear-gradient(#00ff99, #009966);", GasType.REGULAR, showReceiptScreen);
        Button plusBtn = createGasButton("Plus", baseStyle + "-fx-background-color: linear-gradient(#3399ff, #003366);", GasType.PLUS, showReceiptScreen);
        Button premiumBtn = createGasButton("Premium", baseStyle + "-fx-background-color: linear-gradient(#ff6600, #993300);", GasType.PREMIUM, showReceiptScreen);

        hbox.getChildren().addAll(regularBtn, plusBtn, premiumBtn);

        return new Scene(hbox, 800, 600, Color.BLACK);
    }

    private Button createGasButton(String text, String style, GasType type, Runnable showReceiptScreen) {
        Button btn = new Button(text);
        btn.setStyle(style);
        btn.setOnAction(e -> {
            sendMessageToPump(type); // Keep message sending
            System.out.println("Selected: " + type);
            showReceiptScreen.run(); // Switch to receipt
        });
        return btn;
    }

    private void sendMessageToPump(GasType type) {

    }
}