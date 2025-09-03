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

    //TODO: super important TODO, make sure to have getters and/or
    //TODO continued: setters for these global variables

    private Map<Integer, Button> buttonMap = new HashMap<>();
    private GridPane centerPane;
    private Map<String, Node> nodeMap = new HashMap<>();
    private Map<Integer, Node> stackMap = new HashMap<>();
    private Node mergedNode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = createSideButtons();
        root.setCenter(createMiddle());

        addStackPanes();

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
        //getVisualBounds means it excludes taskbars/docs
        //for absolute full resolution, including areas covered by taskbar, use getBounds()
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

    private void addStackPanes(){
        int index = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 2; col++) {

                StackPane s = new StackPane();
                Label l = new Label();

                s.getChildren().add(l);
                centerPane.add(s, col, row);

                stackMap.put(index, s);
                nodeMap.put(""+index, l);
                index++;
            }
        }
    }
    private void createLabel(int height, int width, int section){
        Label label = new Label();
        GridPane.setColumnSpan(label, width);
        GridPane.setRowSpan(label, height);

        stackMap.get(section).getChildren().add(label);


    }




    //TODO: need to pass string value (for map) of which label to add text to
    //TODO: add label to map (perform same action in removeText())
    private void writeText(){
        mergedNode = new Label("Question being posed");
        GridPane.setColumnSpan(mergedNode, 2);
        GridPane.setHalignment(mergedNode, HPos.CENTER);
        centerPane.add(mergedNode, 0, 0);


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
    private void removeText(){
        GridPane.setColumnSpan(mergedNode, 1);
        centerPane.getChildren().remove(mergedNode);
    }
    public void changeFont(String fontStyle, int numNode){
        if(fontStyle.equals("i")){
            nodeMap.get(""+numNode).setStyle("-fx-font-style: italic");
        }else if(fontStyle.equals("b")){
            nodeMap.get(""+numNode).setStyle("-fx-font-style: normal");
            nodeMap.get(""+numNode).setStyle("-fx-font-weight: bold");
        }else if(fontStyle.equals("n")){
            nodeMap.get(""+numNode).setStyle("-fx-font-style: normal");
        }
    }
    public void changeTextSize(String num, int numNode){
        nodeMap.get(""+numNode).setStyle("-fx-font-size: " + num + ";");
    }

    public void changeButtonColor(String s, int buttonNum){
        buttonMap.get(buttonNum).setStyle("-fx-background-color: " + s + ";");
    }

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
    public void giveButtonAction(int action, int button){
        buttonMap.get(button).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

}
