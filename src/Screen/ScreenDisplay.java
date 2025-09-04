//package Screen;
//import javafx.scene.layout.*;
//
//import java.io.BufferedReader;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class ScreenDisplay {
//
//
//    private final Pane screenPane;
//    public ScreenDisplay() {
//        screenPane =  new Pane();
//
//    }
//
//    public Pane getScreenPane() {
//        return screenPane;
//    }
//}
package Screen;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


public class ScreenDisplay extends Application {

    // Shows the possible actions that a button can have
    public enum PossibleActionsForButton {
        CHOOSE_GAS_TYPE_ONE(0),
        CHOSE_GAS_TYPE_TWO(1),
        CHOOSE_GAS_TYPE_THREE(2),
        ACCEPT_RECEIPT(3),
        DENY_RECEIPT(4),
        CANCEL(5);

        private final int actionNum;

        PossibleActionsForButton(int actionNum) {
            this.actionNum = actionNum;
        }

        public int getActionNum() {
            return actionNum;
        }
    }

    //TODO: super important TODO, make sure to have getters and/or
    //TODO continued: setters for these global variables
    private Screen screenHandler; // Reference to Screen class to send messages

    private Map<Integer, Button> buttonMap = new HashMap<>();
    private GridPane centerPane;
    private Map<String, Label> labelMap = new HashMap<>();
    private Map<Integer, StackPane> stackMap = new HashMap<>();
    private Node mergedNode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        showGasSelectionScreen(primaryStage);
    }

    private void showGasSelectionScreen(Stage primaryStage) {
        BorderPane root = createSideButtons(); // Keep side buttons
        root.setCenter(createMiddle());        // Same middle layout
        addMidLabels();

        // First label: instruction
        changeLabel(1, 2, 0);  // Span 1 row, 2 columns
        Label titleLabel = labelMap.get("0");
        titleLabel.setText("SELECT GAS:");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("Verdana", 40));
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setStyle("-fx-background-color: #111111; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Gas option labels stacked vertically
        changeLabel(1, 2, 2);  // REGULAR
        Label regLabel = labelMap.get("2");
        regLabel.setText("REGULAR");
        regLabel.setTextFill(Color.WHITE);
        regLabel.setFont(Font.font("Verdana", 40));
        regLabel.setAlignment(Pos.CENTER);
        regLabel.setStyle("-fx-background-color: #111111; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        changeLabel(1, 2, 4);  // PLUS (next row down)
        Label plusLabel = labelMap.get("4");
        plusLabel.setText("PLUS");
        plusLabel.setTextFill(Color.WHITE);
        plusLabel.setFont(Font.font("Verdana", 40));
        plusLabel.setAlignment(Pos.CENTER);
        plusLabel.setStyle("-fx-background-color: #111111; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        changeLabel(1, 2, 6);  // PREMIUM (next row down)
        Label premLabel = labelMap.get("6");
        premLabel.setText("PREMIUM");
        premLabel.setTextFill(Color.WHITE);
        premLabel.setFont(Font.font("Verdana", 40));
        premLabel.setAlignment(Pos.CENTER);
        premLabel.setStyle("-fx-background-color: #111111; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Assign side buttons to gas options
        setupGasButton(2, PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE, "REGULAR", "forestgreen", primaryStage);
        setupGasButton(4, PossibleActionsForButton.CHOSE_GAS_TYPE_TWO, "PLUS", "dodgerblue", primaryStage);
        setupGasButton(6, PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE, "PREMIUM", "orangered", primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // Modified to go to receipt after selecting gas
    private void setupGasButton(int buttonNum, PossibleActionsForButton action, String label, String color, Stage primaryStage) {
        Button btn = buttonMap.get(buttonNum);
        if (btn == null) return;

        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px;");

        btn.setOnAction(e -> {
            // Keep your original message functionality
            System.out.println("Button " + buttonNum + " pressed: " + action.name());
            if (screenHandler != null) {
                // Example:
                // screenHandler.sendMessage(new Message("SC-BUTTON-" + buttonNum + "-" + action.name()));
            }

            // After selecting gas type → go to receipt screen
            showReceiptScreen(primaryStage);
        });
    }

    private void showReceiptScreen(Stage primaryStage) {
        BorderPane root = createSideButtons();
        root.setCenter(createMiddle());
        addMidLabels();

        changeLabel(1, 2, 0);
        writeText("Would you like a receipt?", 0);

        Label yes = labelMap.get("2");
        yes.setText("YES");
        yes.setTextFill(Color.WHITE);
        yes.setFont(Font.font("Verdana", 40));
        yes.setAlignment(Pos.CENTER_LEFT);
        yes.setStyle("-fx-background-color: #111111; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        Label no = labelMap.get("3");
        no.setText("NO");
        no.setTextFill(Color.WHITE);
        no.setFont(Font.font("Verdana", 40));
        no.setAlignment(Pos.CENTER_RIGHT);
        no.setStyle("-fx-background-color: #111111; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        changeButtonColor("green", 2);
        changeButtonColor("red", 3);

        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    // Method creates buttons 0-9 (placed on left and right side of screen)
    // Occupies left and right side of BorderPane
    private BorderPane createSideButtons() {
        BorderPane root = new BorderPane();
        VBox left = new VBox();
        VBox right = new VBox();
        //getVisualBounds means it excludes taskbars/docs
        //for absolute full resolution, including areas covered by taskbar, use getBounds()
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        for (int i = 0; i < 10; i++) {
            Button b = new Button("" + i);
            buttonMap.put(i, b);
            b.setPrefSize(200, screenBounds.getHeight() / 5);
            //Code for unbordered labels
//            b.setStyle("-fx-alignment: bottom-right;");
//            b.setStyle("-fx-background-color: #1E1E1E;");
            b.setStyle(
                    "-fx-background-color: #1E1E1E;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 5;"
            );
            if (i % 2 == 0) {
                left.getChildren().add(b);
            } else {
                right.getChildren().add(b);
            }
        }
        root.setLeft(left);
        root.setRight(right);


        // Assign first 3 buttons to gas options
//        setupGasButton(0, PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE, "Regular", "forestgreen");
//        setupGasButton(1, PossibleActionsForButton.CHOSE_GAS_TYPE_TWO, "Plus", "dodgerblue");
//        setupGasButton(2, PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE, "Premium", "orangered");

        return root;
    }

    private void addGasButtonsToCenter() {
        String[] gasNames = { "Regular", "Plus", "Premium" };
        String[] gasColors = { "forestgreen", "dodgerblue", "orangered" };
        PossibleActionsForButton[] actions = {
                PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE,
                PossibleActionsForButton.CHOSE_GAS_TYPE_TWO,
                PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE
        };

        for (int i = 0; i < 3; i++) {
            Button gasBtn = new Button(gasNames[i]);
            gasBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gasBtn.setStyle(
                    "-fx-background-color: " + gasColors[i] + ";" +
                            "-fx-text-fill: white; -fx-font-size: 20px;"
            );

            int buttonIndex = i; // for lambda
            gasBtn.setOnAction(e -> {
                System.out.println("Gas button pressed: " + actions[buttonIndex]);
                if (screenHandler != null) {
                    // screenHandler.sendMessage(new Message(...));
                }
            });

            // Row 1, 2, 3 in column 0
            centerPane.add(gasBtn, 0, i + 1);
        }
    }

    // TODO: need to finish/work on message sending from buttons and test
    // Assign action and label to a button
    private void setupGasButton(int buttonNum, PossibleActionsForButton action, String label, String color) {
        Button btn = buttonMap.get(buttonNum);
        if (btn == null) return;

        btn.setText(label);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px;");

        btn.setOnAction(e -> {
            System.out.println("Button " + buttonNum + " pressed: " + action.name());
            if (screenHandler != null) {
                //screenHandler.sendMessage(new Message("SC-BUTTON-" + buttonNum + "-" + action.name()));
            }
        });
    }

    // Creates a 2x5 grid on GridPane.
    // GridPane occupies entire center of BorderPane
    private GridPane createMiddle() {
        centerPane = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        centerPane.getColumnConstraints().addAll(col1, col2);

        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            centerPane.getRowConstraints().add(row);
        }
        return centerPane;
    }

    // Previously known as addStackPanes
    // Adds labels to each cell of the 2x5 grid
    private void addMidLabels() {
        int index = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 2; col++) {

                Label l = new Label();
                l.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
                l.setStyle("-fx-background-color: #111111;");
                /////////////
//                if(index % 2 == 0){
//                    l.setStyle("-fx-border-color: black;");
//                }else{
//                    l.setStyle("-fx-border-color: orange;");
//                }
                /////////////

                centerPane.add(l, col, row);

                labelMap.put("" + index, l);
                index++;
            }
        }
    }

    // Previously known as createLabel(int height, int width, int section)
    // Changes the size of a specified label to a specified height and width
    private void changeLabel(int height, int width, int section) {


        Label l = labelMap.get("" + section);

        int startRow = section / 2;
        int startCol = section % 2;


        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int index = (startRow + row) * 2 + (startCol + col);
                if (index != section) {
                    Label below = labelMap.get("" + index);
                    below.setStyle("-fx-background-color: transparent;"); // or transparent
                    System.out.println(index);
                }
            }
        }


        l.setStyle("-fx-background-color: white;");
        GridPane.setColumnSpan(l, width);
        GridPane.setRowSpan(l, height);

        ////// This is used for testing purposes
//        l.setStyle("-fx-background-color: green;");
//        l.setText("dddd");
        //////

        l.setAlignment(Pos.CENTER);
    }

    //TODO: need to pass string value (for map) of which label to add text to
    //TODO: add label to map (perform same action in removeText())

    //TODO: change this method
    private void writeText(String text, int label) {
        Label l = labelMap.get("" + label);
        l.setText(text);
        changeTextSize("40", 0);


        // This is example code
//        for (int row = 1; row < 5; row++) {
//            for (int col = 0; col < 2; col++) {
//                Button btn = new Button("R" + row + "C" + col);
//                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//                centerPane.add(btn, col, row);
//            }
//        }
    }

    //TODO: need to pass string value (for map) of which label to remove text from
    private void removeText() {
        GridPane.setColumnSpan(mergedNode, 1);
        centerPane.getChildren().remove(mergedNode);
    }

    // Changes the font of specified label
    public void changeFont(String fontStyle, int numNode) {
        if (fontStyle.equals("i")) {
            labelMap.get("" + numNode).setStyle("-fx-font-style: italic");
        } else if (fontStyle.equals("b")) {
            labelMap.get("" + numNode).setStyle("-fx-font-style: normal");
            labelMap.get("" + numNode).setStyle("-fx-font-weight: bold");
        } else if (fontStyle.equals("n")) {
            labelMap.get("" + numNode).setStyle("-fx-font-style: normal");
        }
    }

    // Changes text size of specified label
    public void changeTextSize(String num, int numNode) {
        labelMap.get("" + numNode).setStyle("-fx-font-size: " + num + ";");
    }

    // Changes button color of specified number
    public void changeButtonColor(String s, int buttonNum) {
        buttonMap.get(buttonNum).setStyle("-fx-background-color: " + s + ";");
    }

    // Converts color representation from message received to actual color
    public String convertColor(String color) {
        if (color == null) {
            return "transparent";
        }
        switch (color) {
            case "re":
                return "crimson";
            case "gr":
                return "forestgreen";
            case "bl":
                return "deepskyblue";
            case "ye":
                return "yellow";
            case "or":
                return "orangered";
            case "pu":
                return "darkslateblue";
            case "go":
                return "goldenrod";
            case "pi":
                return "thistle";
            case "cy":
                return "aquamarine";
            default:
                return "ghostwhite";
        }
    }

    //TODO: Finish this
    public void giveButtonAction(int action, int button) {
        buttonMap.get(button).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }
}
