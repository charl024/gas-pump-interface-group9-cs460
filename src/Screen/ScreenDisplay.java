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
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;



public class ScreenDisplay extends Application {

//    public enum ButtonColor {
//        RED, GREEN, BLUE, YELLOW, ORANGE, PURPLE, GOLD, PINK, CYAN, BLACK
//    }

    public enum PossibleActionsForButton{
        //0.
        CHOOSE_GAS_TYPE_ONE(0),
        //1.
        CHOSE_GAS_TYPE_TWO(1),
        //2.
        CHOOSE_GAS_TYPE_THREE(2),
        //3.
        ACCEPT_RECEIPT(3),
        //4.
        DENY_RECEIPT(4),
        //5.
        CANCEL(5);
        //6.

        private final int actionNum;

        PossibleActionsForButton(int actionNum){
            this.actionNum = actionNum;
        }

        public int getActionNum(){
            return this.actionNum;
        }


    }

    Map<Integer, Button> buttonMap = new HashMap<>();
    GridPane centerPane;
    //TODO: might need to create an array to hold all nodes/Labels
    //TODO continued: placed on the screen
    Node mergedNode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = createSideButtons();
        root.setCenter(createMiddle());
        writeText();
//        removeText();





        primaryStage.setScene(new Scene(root));
        ///////
//        primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        ///////
        primaryStage.show();

    }

    private BorderPane createSideButtons(){
        BorderPane root = new BorderPane();
        VBox left = new VBox();
        VBox right = new VBox();
        //TODO: getVisualBounds means it excludes taskbars/docs
        //TODO: for absolute full resolution, including areas covered by taskbar, use getBounds()
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        for(int i = 0; i < 10; i++){
            Button b = new Button(""+i);
            buttonMap.put(i, b);
            b.setPrefSize(200, screenBounds.getHeight() / 5);
            b.setStyle("-fx-alignment: bottom-right;");



            if(i % 2 == 0){
                left.getChildren().add(b);
            }else{
                right.getChildren().add(b);
            }
        }
        root.setLeft(left);
        root.setRight(right);
        return root;
    }


    private GridPane createMiddle(){
        centerPane = new GridPane();


//        gridPane.setHgap(5);
//        gridPane.setVgap(5);
//        gridPane.setPadding(new Insets(10));


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        centerPane.getColumnConstraints().addAll(col1, col2);

        for(int i = 0; i < 5; i++){
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            centerPane.getRowConstraints().add(row);
        }
        return centerPane;
    }


    //TODO: Decide whether to pass and return gridPane (Center Portion)
    //TODO: or entire GridPane
    private void writeText(){
        //TODO: set current text style to preferred style
        mergedNode = new Label("Question being posed");
        GridPane.setColumnSpan(mergedNode, 2);
        GridPane.setHalignment(mergedNode, HPos.CENTER);
        centerPane.add(mergedNode, 0, 0);


        // This is example code
        for (int row = 1; row < 5; row++) {
            for (int col = 0; col < 2; col++) {
                Button btn = new Button("R" + row + "C" + col);
                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                centerPane.add(btn, col, row);
            }
        }
    }

    //TODO: Decide whether to pass and return gridPane (Center Portion)
    //TODO: or entire GridPane

    //TODO maybe change Node parameter to a better choice, likely Label object

    //TODO: If needing to remove specific node, keep track of exact reference of that object
    private void removeText(){
        GridPane.setColumnSpan(mergedNode, 1);
        centerPane.getChildren().remove(mergedNode);
    }
    private void changeFont(char fontStyle){
        if(fontStyle == 'i'){
            mergedNode.setStyle("-fx-font-style: italic");
        }else if(fontStyle == 'b'){
            mergedNode.setStyle("-fx-font-style: normal");
            mergedNode.setStyle("-fx-font-weight: bold");
        }else if(fontStyle == 'n'){
            mergedNode.setStyle("-fx-font-style: normal");
        }
    }
    private void changeTextSize(String num){
        mergedNode.setStyle("-fx-font-size: " + num + ";");
    }

    private void changeButtonColor(int num, String s){
        buttonMap.get(num).setStyle("-fx-background-color: " + s + ";");
    }

    //TODO:
    private String convertColor(String color) {
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

//    private void giveButtonAction(int button, int action){
//        buttonMap.get(button).setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//
//            }
//        });
//    }

}
