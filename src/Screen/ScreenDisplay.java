package Screen;


import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class ScreenDisplay {

    // Shows the possible actions that a button can have
    public enum PossibleActionsForButton {
        CHOOSE_GAS_TYPE_ONE(0), CHOSE_GAS_TYPE_TWO(1), CHOOSE_GAS_TYPE_THREE(2), ACCEPT_RECEIPT(3), DENY_RECEIPT(4), CANCEL(5);

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
    private Color originalMidColor = Color.web("#111111");
    private Color originalButtonColor = Color.web("#1E1E1E");

    private Consumer<Integer> onAction;

    public void showScreen(Stage primaryStage) {
        BorderPane root = createSideButtons();
        root.setCenter(createMiddle());
        addMidLabels();

        // Method calls used to display certain screens
        // and to test out resetDisplay method (seems to work well now)
//        showWelcomeScreen();
//        resetLabels();
//        showAuthorizationScreen();
//        resetLabels();
//        showCardDeniedScreen();
//        resetLabels();
//        showCardAcceptedScreen();
//        resetLabels();
//        showGasSelectionScreen();
//        resetLabels();
//        showConnectHoseScreen();
//        resetLabels();
        showPumpingProgress();
//        resetLabels();
//        showHosePausedScreen();
//        resetLabels();
//        showFuelFinishedScreen(48.9, 100.3);
//        resetLabels();
//        showReceiptScreen();
//        resetLabels();
//        showPumpUnavailableScreen();
//        resetLabels();


        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
    }

    ///////////////////////////////////////////////////////////////////////////
    //CREATE GENERAL THINGS, SIDE BUTTONS, AND MIDDLE SECTION
    ///////////////////////////////////////////////////////////////////////////

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
//            Button b = new Button("" + i);
            Button b = new Button();
            buttonMap.put(i, b);
            b.setPrefSize(200, screenBounds.getHeight() / 5);
            b.setTextFill(Color.WHITE);
            b.setFont(Font.font("Verdana", 16));
            //Code for unbordered labels
            changeButtonColorV2(originalButtonColor, i);
            b.setStyle("-fx-border-color: white;" + "-fx-border-width: 2;" + "-fx-border-radius: 5;");
            if (i % 2 == 0) {
                left.getChildren().add(b);
            } else {
                right.getChildren().add(b);
            }
        }
        root.setLeft(left);
        root.setRight(right);

        return root;
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
                l.setBackground(new Background(new BackgroundFill(originalMidColor, CornerRadii.EMPTY, Insets.EMPTY)));

                centerPane.add(l, col, row);

                labelMap.put("" + index, l);
                index++;
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    //CREATE THE TYPE OF SCREENS THAT WILL BE SHOWN
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Shows the welcome screen
     */
    public void showWelcomeScreen() {
        changeLabel(5, 2, 0);  // Span 1 row, 2 columns
        Label main = labelMap.get("0");
        writeText("Welcome to _______!\nPlease tap your card", 0);
        main.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        main.setAlignment(Pos.CENTER);
        main.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");
    }

    /**
     * Shows screen indicating that the user's card is currently being
     * authorized
     */
    public void showAuthorizationScreen() {
        // Maybe Color.POWDERBLUE
        Color colorForScreen = Color.SKYBLUE;

        changeLabel(5, 2, 0);  // Span 1 row, 2 columns
        Label accept = labelMap.get("0");
        writeText("Authorizing...", 0);
        accept.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        accept.setAlignment(Pos.CENTER);
        accept.setBackground(new Background(new BackgroundFill(colorForScreen, CornerRadii.EMPTY, Insets.EMPTY)));
        accept.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(colorForScreen, i);
        }
    }

    /**
     * Shows screen indicating that the user's payment was successful
     */
    public void showCardAcceptedScreen() {
        changeLabel(5, 2, 0);  // Span 1 row, 2 columns
        Label accept = labelMap.get("0");
        writeText("Your payment was successful!", 0);
        accept.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        accept.setAlignment(Pos.CENTER);
        accept.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        accept.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.GREEN, i);
        }
    }

    /**
     * Shows screen indicating that the user's payment was denied
     */
    public void showCardDeniedScreen() {
        changeLabel(5, 2, 0);  // Span 1 row, 2 columns
        Label accept = labelMap.get("0");
        writeText("Warning: Card has been denied", 0);
        accept.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        accept.setAlignment(Pos.CENTER);
        accept.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        accept.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");


        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.DARKRED, i);
        }
    }

    /**
     * Shows screen showing the gas options
     */
    public void showGasSelectionScreen() {
        //TODO INCLUDE THE PRICE PER GALLON TOO

        // First label: instruction
        changeLabel(1, 2, 0);  // Span 1 row, 2 columns
        Label titleLabel = labelMap.get("0");
        writeText("SELECT GAS:", 0);
        titleLabel.setTextFill(Color.WHITE);
        changeTextSize(40, 0);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        titleLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Gas option labels stacked vertically
        changeLabel(1, 2, 2);  // Span 1 row, 2 columns
        Label regLabel = labelMap.get("2");
        writeText("REGULAR", 2);
        regLabel.setTextFill(Color.WHITE);
        changeTextSize(40, 2);
        regLabel.setAlignment(Pos.CENTER);
        regLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        regLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");


        changeLabel(1, 2, 4);  // Span 1 row, 2 columns
        Label plusLabel = labelMap.get("4");
        writeText("PLUS", 4);
        plusLabel.setTextFill(Color.WHITE);
        changeTextSize(40, 4);
        plusLabel.setAlignment(Pos.CENTER);
        plusLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        plusLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        changeLabel(1, 2, 6);  // Span 1 row, 2 columns
        Label premLabel = labelMap.get("6");
        writeText("PREMIUM", 6);
        premLabel.setTextFill(Color.WHITE);
        changeTextSize(40, 6);
        premLabel.setAlignment(Pos.CENTER);
        premLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        premLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Assign side buttons to gas options
        setUpButtonPress(2, PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE, Color.FORESTGREEN);
        setUpButtonPress(4, PossibleActionsForButton.CHOSE_GAS_TYPE_TWO, Color.DEEPSKYBLUE);
        setUpButtonPress(6, PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE, Color.ORANGE);
        setUpButtonPress(3, PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE, Color.FORESTGREEN);
        setUpButtonPress(5, PossibleActionsForButton.CHOSE_GAS_TYPE_TWO, Color.DEEPSKYBLUE);
        setUpButtonPress(7, PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE, Color.ORANGE);
    }

    /**
     * Show screen telling the user to please connect the hose to the gas tank
     */
    public void showConnectHoseScreen() {
        // Maybe Color.POWDERBLUE
        Color colorForScreen = Color.SKYBLUE;
        changeLabel(5, 2, 0);  // Span 1 row, 2 columns
        Label accept = labelMap.get("0");
        writeText("Please connect hose", 0);
        accept.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        accept.setAlignment(Pos.CENTER);
//        accept.setBackground(new Background(new BackgroundFill(colorForScreen,  CornerRadii.EMPTY, Insets.EMPTY)));
        accept.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

//        for(int i = 0; i < 10; i++){
//            changeButtonColorV2(colorForScreen, i);
//        }
    }

    /**
     * Show screen telling the user that gas is currently being pumped out
     */
    public void showPumpingProgress() {
        Color colorForScreen = Color.SKYBLUE;
        changeLabel(5, 2, 0);  // Span 1 row, 2 columns
        Label accept = labelMap.get("0");
        writeText("Pumping in Progress...\nPlease wait.", 0);
        accept.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        accept.setAlignment(Pos.CENTER);
        accept.setBackground(new Background(new BackgroundFill(colorForScreen, CornerRadii.EMPTY, Insets.EMPTY)));
        accept.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(colorForScreen, i);
        }
    }

    /**
     * Show screen telling the user that pumping gas has been paused
     */
    public void showHosePausedScreen() {
        // Maybe Color.POWDERBLUE
        Color colorForScreen = Color.SKYBLUE;
        changeLabel(5, 2, 0);  // Span 1 row, 2 columns
        Label accept = labelMap.get("0");
        writeText("Gas pumping has been paused", 0);
        accept.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        accept.setAlignment(Pos.CENTER);
        accept.setBackground(new Background(new BackgroundFill(colorForScreen, CornerRadii.EMPTY, Insets.EMPTY)));
        accept.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(colorForScreen, i);
        }
    }


    /**
     * Show screen indicating the final total and the amount of gas that was
     * pumped out
     *
     * @param totalGallons Total gas pumped out
     * @param totalPrice Cost of gas pumped out
     */
    public void showFuelFinishedScreen(double totalGallons, double totalPrice) {
        // Fill the entire middle with one big label
        changeLabel(5, 2, 0); // Span 5 rows, 2 columns
        Label main = labelMap.get("0");

        // Main message
        writeText(String.format("Pumping Complete\nTotal Gallons: %.2f\nTotal Price: $%.2f", totalGallons, totalPrice), 0);

        main.setTextFill(Color.WHITE);
        changeTextSize(60, 0);
        main.setAlignment(Pos.CENTER);
        main.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        main.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Change all side buttons to a consistent color
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.DARKBLUE, i);
        }
    }

    //TODO: DONT NEED THIS ANYMORE, CAN DELETE
//    /**
//     * Shows receipt screen
//     */
//    public void showReceiptScreen() {
//
//        changeLabel(1, 2, 0);
//        writeText("Would you like a receipt?", 0);
//
//        Label yes = labelMap.get("2");
//        writeText("YES", 2);
//        yes.setTextFill(Color.WHITE);
//        changeTextSize(40, 2);
//        yes.setAlignment(Pos.CENTER_LEFT);
//        yes.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
//        yes.setStyle("-fx-border-width: 2; -fx-border-radius: 5;");
//
//        Label no = labelMap.get("3");
//        writeText("NO", 3);
//        no.setTextFill(Color.WHITE);
//        changeTextSize(40, 2);
//        no.setAlignment(Pos.CENTER_LEFT);
//        no.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
//        no.setStyle("-fx-border-width: 2; -fx-border-radius: 5;");
//
////        changeButtonColor("green", 2);
//        setUpButtonPress(2, PossibleActionsForButton.ACCEPT_RECEIPT, Color.FORESTGREEN);
////        changeButtonColor("red", 3);
//        setUpButtonPress(3, PossibleActionsForButton.DENY_RECEIPT, Color.CRIMSON);
//    }

    /**
     * Shows screen that indicates that the pump is currently unavailable
     */
    public void showPumpUnavailableScreen() {
        changeLabel(5, 2, 0);  // Span all rows, 2 columns
        Label unavailable = labelMap.get("0");
        writeText("Pump Unavailable\nPlease see cashier", 0);
        unavailable.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        unavailable.setAlignment(Pos.CENTER);
        unavailable.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        unavailable.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Change all side buttons to gray
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.DARKGRAY, i);
        }
    }
    public void showDisconnectScreen() {
        changeLabel(5, 2, 0);  // Span all rows, 2 columns
        Label unavailable = labelMap.get("0");
        writeText("Disconnected\nPlease see cashier", 0);
        unavailable.setTextFill(Color.WHITE);
        changeTextSize(80, 0);
        unavailable.setAlignment(Pos.CENTER);
        unavailable.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        unavailable.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Change all side buttons to gray
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.DARKGRAY, i);
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    //SECTION THAT HANDLES MAKING SPECIFIC CHANGES TO A BUTTON OR A LABEL
    ///////////////////////////////////////////////////////////////////////////

    // TODO: need to finish/work on message sending from buttons and test
    // Modified to go to receipt after selecting gas
    private void setUpButtonPress(int buttonNum, PossibleActionsForButton action /*, String label*/, Color color) {
        Button btn = buttonMap.get(buttonNum);
        if (btn == null) return;

        changeButtonColorV2(color, buttonNum);

        btn.setOnAction(e -> {
            // Keep your original message functionality
            System.out.println("Button " + buttonNum + " pressed: " + action.name());
            if (screenHandler != null) {
                // Example:
                // screenHandler.sendMessage(new Message("SC-BUTTON-" + buttonNum + "-" + action.name()));
                onAction.accept(action.actionNum);
            }

            // After selecting gas type → go to receipt screen
            // May need to first receive a message in order to progress to receipt screen
//            showReceiptScreen();
        });
    }

    private void addGasButtonsToCenter() {
        String[] gasNames = {"Regular", "Plus", "Premium"};
        String[] gasColors = {"forestgreen", "dodgerblue", "orangered"};
        PossibleActionsForButton[] actions = {PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE, PossibleActionsForButton.CHOSE_GAS_TYPE_TWO, PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE};
        for (int i = 0; i < 3; i++) {
            Button gasBtn = new Button(gasNames[i]);
            gasBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gasBtn.setStyle("-fx-background-color: " + gasColors[i] + ";" + "-fx-text-fill: white; -fx-font-size: 20px;");

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
                    below.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))); // or transparent
                    System.out.println(index);
                }
            }
        }

//        l.setBackground(new Background(new BackgroundFill(Color.WHITE,
//                CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setColumnSpan(l, width);
        GridPane.setRowSpan(l, height);

        ////// This is used for testing purposes
//        l.setStyle("-fx-background-color: green;");
//        l.setText("dddd");
        //////

        l.setAlignment(Pos.CENTER);
    }

    // Use this whenever you enlarge a label and want to revert back to original layout
    public void resetLabels() {
        for (int i = 0; i < 10; i++) {
            Label l = labelMap.get("" + i);
            GridPane.setColumnSpan(l, 1);
            GridPane.setRowSpan(l, 1);
            writeText("", i);
            l.setBackground(new Background(new BackgroundFill(originalMidColor, CornerRadii.EMPTY, Insets.EMPTY)));
            l.setStyle("");

        }
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(originalButtonColor, i);
            buttonMap.get(i).setTextFill(Color.WHITE);
        }

    }

    //TODO: need to pass string value (for map) of which label to add text to
    //TODO: add label to map (perform same action in removeText())

    //TODO: change this method
    public void writeText(String text, int label) {
        Label l = labelMap.get("" + label);
        l.setText(text);
    }

    //TODO: need to pass string value (for map) of which label to remove text from
    private void removeText(int label) {
        Label l = labelMap.get("" + label);
        l.setText("");
    }

    // Changes the font of specified label
    public void changeFont(String fontStyle, int numNode) {
        Label l = labelMap.get("" + numNode);
        Font current = l.getFont();
        FontWeight weight = current.getStyle().contains("Bold") ? FontWeight.BOLD : FontWeight.NORMAL;
        FontPosture posture = current.getStyle().contains("Italic") ? FontPosture.ITALIC : FontPosture.REGULAR;

        if (fontStyle.equals("i")) {
            posture = FontPosture.ITALIC;
            weight = FontWeight.NORMAL;
        } else if (fontStyle.equals("b")) {
            weight = FontWeight.BOLD;
            posture = FontPosture.REGULAR;
        } else if (fontStyle.equals("n")) {
            weight = FontWeight.NORMAL;
            posture = FontPosture.REGULAR;
        }
        l.setFont(Font.font(current.getFamily(), weight, posture, current.getSize()));
    }

    // Changes text size of specified label
    public void changeTextSize(int size, int numNode) {
        Label l = labelMap.get("" + numNode);
        Font current = l.getFont();
        l.setFont(Font.font(current.getFamily(), current.getStyle().contains("Bold") ? FontWeight.BOLD : FontWeight.NORMAL, current.getStyle().contains("Italic") ? FontPosture.ITALIC : FontPosture.REGULAR, size));
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

    public Color convertColorV2(String color) {
        if (color == null) {
            return Color.TRANSPARENT;
        }
        switch (color) {
            case "re":
                return Color.CRIMSON;
            case "gr":
                return Color.FORESTGREEN;
            case "bl":
                return Color.DEEPSKYBLUE;
            case "ye":
                return Color.YELLOW;
            case "or":
                return Color.ORANGERED;
            case "pu":
                return Color.DARKSLATEBLUE;
            case "go":
                return Color.GOLDENROD;
            case "pi":
                return Color.THISTLE;
            case "cy":
                return Color.AQUAMARINE;
            default:
                return Color.GHOSTWHITE;
        }
    }

    public void changeButtonColorV2(Color color, int buttonNum) {
        buttonMap.get(buttonNum).setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    //TODO: Finish this
    public void giveButtonAction(int action, int button) {
        buttonMap.get(button).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    public void setOnAction(Consumer<Integer> handler) {
        this.onAction = handler;
    }

}
