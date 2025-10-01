package Screen;

import Util.Message;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ScreenDisplay {

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
    }

    private Screen screenHandler; // Reference to Screen class to send messages

    // Screen Variables
    private Map<Integer, Button> buttonMap = new HashMap<>();
    private GridPane centerPane;
    private Map<String, Label> labelMap = new HashMap<>();
    private Map<Integer, StackPane> stackMap = new HashMap<>();
    private Node mergedNode;
    private Color originalMidColor = Color.web("#111111");
    private Color originalButtonColor = Color.web("#1E1E1E");

    private Consumer<Integer> onAction;

    // Gas Variables
    private Label regLabel;
    private Label plusLabel;
    private Label premLabel;
    private double regPrice;
    private double plusPrice;
    private double premPrice;

    private Label cancelLabel;

    private double inUseReg;
    private double inUsePlus;
    private double inUsePrem;

    private boolean onGasSelection = false;
    private HandleMessage handleMessage;

    public void showScreen(Stage primaryStage) {
        BorderPane root = createSideButtons();
        root.setCenter(createMiddle());
        addMidLabels();

        primaryStage.setScene(new Scene(root, 697, 534));

        primaryStage.setTitle("Futuristic Pump Interface");
    }

    ///////////////////////////////////////////////////////////////////////////
    //CREATE GENERAL THINGS, SIDE BUTTONS, AND MIDDLE SECTION

    /// ////////////////////////////////////////////////////////////////////////

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
        changeTextSize(20, 0);
        main.setAlignment(Pos.CENTER);
        main.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");
        onGasSelection = false;
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
        changeTextSize(20, 0);
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
        changeTextSize(20, 0);
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
        changeTextSize(20, 0);
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

        // First label: instruction
        changeLabel(1, 2, 0);  // Span 1 row, 2 columns
        Label titleLabel = labelMap.get("0");
        writeText("SELECT GAS:", 0);
        titleLabel.setTextFill(Color.WHITE);
        changeTextSize(20, 0);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        titleLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Gas option labels stacked vertically
        changeLabel(1, 2, 2);  // Span 1 row, 2 columns
        regLabel = labelMap.get("2");
        writeText("REGULAR - $" + String.format("%.2f", regPrice), 2);
        regLabel.setTextFill(Color.WHITE);
        changeTextSize(20, 2);
        regLabel.setAlignment(Pos.CENTER);
        regLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        regLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");


        changeLabel(1, 2, 4);  // Span 1 row, 2 columns
        plusLabel = labelMap.get("4");
        writeText("PLUS - $" + String.format("%.2f", plusPrice), 4);
        plusLabel.setTextFill(Color.WHITE);
        changeTextSize(20, 4);
        plusLabel.setAlignment(Pos.CENTER);
        plusLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        plusLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        changeLabel(1, 2, 6);  // Span 1 row, 2 columns
        premLabel = labelMap.get("6");
        writeText("PREMIUM - $" + String.format("%.2f", premPrice), 6);
        premLabel.setTextFill(Color.WHITE);
        changeTextSize(20, 6);
        premLabel.setAlignment(Pos.CENTER);
        premLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        premLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        changeLabel(1, 2, 8);
        cancelLabel = labelMap.get("8");
        writeText("CANCEL TRANSACTION", 8);
        cancelLabel.setTextFill(Color.WHITE);
        changeTextSize(20, 8);
        cancelLabel.setAlignment(Pos.CENTER);
        cancelLabel.setBackground(new Background(new BackgroundFill(Color.web("#111111"), CornerRadii.EMPTY, Insets.EMPTY)));
        cancelLabel.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Assign side buttons to gas options
        setUpButtonPress(2, PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE, Color.FORESTGREEN);
        setUpButtonPress(4, PossibleActionsForButton.CHOSE_GAS_TYPE_TWO, Color.DEEPSKYBLUE);
        setUpButtonPress(6, PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE, Color.ORANGE);
        setUpButtonPress(8, PossibleActionsForButton.CANCEL, Color.CRIMSON);
        setUpButtonPress(3, PossibleActionsForButton.CHOOSE_GAS_TYPE_ONE, Color.FORESTGREEN);
        setUpButtonPress(5, PossibleActionsForButton.CHOSE_GAS_TYPE_TWO, Color.DEEPSKYBLUE);
        setUpButtonPress(7, PossibleActionsForButton.CHOOSE_GAS_TYPE_THREE, Color.ORANGE);
        setUpButtonPress(9, PossibleActionsForButton.CANCEL, Color.CRIMSON);

        inUseReg = regPrice;
        inUsePlus = plusPrice;
        inUsePrem = premPrice;
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
        changeTextSize(20, 0);
        accept.setAlignment(Pos.CENTER);
        accept.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");
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
        changeTextSize(20, 0);
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
        changeTextSize(20, 0);
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
     * @param totalPrice   Cost of gas pumped out
     */
    public void showFuelFinishedScreen(double totalPrice, double totalGallons) {
        // Fill the entire middle with one big label
        changeLabel(5, 2, 0); // Span 5 rows, 2 columns
        Label main = labelMap.get("0");

        // Main message
        writeText(String.format("Pumping Complete\nTotal Price: $%.2f\nTotal " +
                "Gallons: %.2f", totalPrice, totalGallons), 0);

        main.setTextFill(Color.WHITE);
        changeTextSize(20, 0);
        main.setAlignment(Pos.CENTER);
        main.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        main.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Change all side buttons to a consistent color
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.DARKBLUE, i);
        }
    }

    /**
     * Shows screen that indicates that the pump is currently unavailable
     */
    public void showPumpUnavailableScreen() {
        changeLabel(5, 2, 0);  // Span all rows, 2 columns
        Label unavailable = labelMap.get("0");
        writeText("Pump Unavailable\nPlease see cashier", 0);
        unavailable.setTextFill(Color.WHITE);
        changeTextSize(20, 0);
        unavailable.setAlignment(Pos.CENTER);
        unavailable.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        unavailable.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Change all side buttons to gray
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.DARKGRAY, i);
        }
    }

    /**
     * Shows screen that indicates that the current action/screen has timed out
     */
    public void showTimeoutScreen() {
        changeLabel(5, 2, 0);  // Span all rows, 2 columns
        Label unavailable = labelMap.get("0");
        writeText("Timed-Out", 0);
        unavailable.setTextFill(Color.WHITE);
        changeTextSize(20, 0);
        unavailable.setAlignment(Pos.CENTER);
        unavailable.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        unavailable.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Change all side buttons to gray
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.DARKRED, i);
        }
    }

    /**
     * Shows screen that indicates that the current transaction has been canceled
     */
    public void showTransactionCanceledScreen() {
        changeLabel(5, 2, 0);  // Span all rows, 2 columns
        Label unavailable = labelMap.get("0");
        writeText("Transaction canceled", 0);
        unavailable.setTextFill(Color.WHITE);
        changeTextSize(20, 0);
        unavailable.setAlignment(Pos.CENTER);
        unavailable.setBackground(new Background(new BackgroundFill(Color.CORAL,
                CornerRadii.EMPTY, Insets.EMPTY)));
        unavailable.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

        // Change all side buttons to gray
        for (int i = 0; i < 10; i++) {
            changeButtonColorV2(Color.CORAL, i);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //SECTION THAT HANDLES MAKING SPECIFIC CHANGES TO A BUTTON OR A LABEL

    /// ////////////////////////////////////////////////////////////////////////

    // Modified to go to receipt after selecting gas
    private void setUpButtonPress(int buttonNum, PossibleActionsForButton action /*, String label*/, Color color) {
        Button btn = buttonMap.get(buttonNum);
        if (btn == null) return;

        changeButtonColorV2(color, buttonNum);

        btn.setOnAction(e -> {
            // Keep your original message functionality
            if (onGasSelection) {
                if (buttonNum == 2 || buttonNum == 3) {
                    Message gasSelection =
                            new Message("SC-GASSELECTION-" + inUseReg);
                    handleMessage.cancelTimeout();
                    resetLabels();
                    showConnectHoseScreen();
                    handleMessage.timeoutTimer();
                    handleMessage.sendServerMessage(gasSelection);
                    onGasSelection = false;
                } else if (buttonNum == 4 || buttonNum == 5) {
                    Message gasSelection =
                            new Message("SC-GASSELECTION-" + inUsePlus);
                    handleMessage.cancelTimeout();
                    resetLabels();
                    showConnectHoseScreen();
                    handleMessage.timeoutTimer();
                    handleMessage.sendServerMessage(gasSelection);
                    onGasSelection = false;
                } else if (buttonNum == 6 || buttonNum == 7) {
                    Message gasSelection = new Message("SC-GASSELECTION-" + inUsePrem);
                    handleMessage.cancelTimeout();
                    resetLabels();
                    showConnectHoseScreen();
                    handleMessage.timeoutTimer();
                    handleMessage.sendServerMessage(gasSelection);
                    onGasSelection = false;
                }
                if (buttonNum == 8 || buttonNum == 9) {
                    handleMessage.resetVars();
                    handleMessage.setWelcomeScreen();
                    resetLabels();
                    handleMessage.transactionCancel();
                    handleMessage.sendServerMessage(new Message("SC-CANCELTRANSACTION"));
                    onGasSelection = false;
                    return;
                }
            }
            if (screenHandler != null) {
                // Example:
                // screenHandler.sendMessage(new Message("SC-BUTTON-" + buttonNum + "-" + action.name()));
                onAction.accept(action.actionNum);
            }
        });
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
                    //System.out.println(index);
                }
            }
        }

        GridPane.setColumnSpan(l, width);
        GridPane.setRowSpan(l, height);
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

    public void writeText(String text, int label) {
        Label l = labelMap.get("" + label);
        l.setText(text);
    }

    // Changes text size of specified label
    public void changeTextSize(int size, int numNode) {
        Label l = labelMap.get("" + numNode);
        Font current = l.getFont();
        l.setFont(Font.font(current.getFamily(), current.getStyle().contains("Bold") ? FontWeight.BOLD : FontWeight.NORMAL, current.getStyle().contains("Italic") ? FontPosture.ITALIC : FontPosture.REGULAR, size));
    }

    public void changeButtonColorV2(Color color, int buttonNum) {
        buttonMap.get(buttonNum).setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Setters and Getters

    /// ////////////////////////////////////////////////////////////////////////

    public void setOnAction(Consumer<Integer> handler) {
        this.onAction = handler;
    }

    public double getRegPrice() {
        return regPrice;
    }

    public double getPlusPrice() {
        return plusPrice;
    }

    public double getPremPrice() {
        return premPrice;
    }

    public void setPrices(double regular, double plus, double premium) {
        this.regPrice = regular;
        this.plusPrice = plus;
        this.premPrice = premium;
    }

    public void updateGasPrices(double regular, double plus, double premium) {
        if (regLabel != null) {
            regLabel.setText("REGULAR: $" + String.format("%.2f", regular));
            this.regPrice = regular;
        }
        if (plusLabel != null) {
            plusLabel.setText("PLUS: $" + String.format("%.2f", plus));
            this.plusPrice = plus;
        }
        if (premLabel != null) {
            premLabel.setText("PREMIUM: $" + String.format("%.2f", premium));
            this.premPrice = premium;
        }
    }

    public void setOnGasSelection() {
        onGasSelection = true;
    }

    public void setHandleMessage(HandleMessage handleMessage) {
        this.handleMessage = handleMessage;
    }
}