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

    Map<Button, Integer> buttonMap = new HashMap<>();
    GridPane centerPane;
    Node mergedNode;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = createSideButtons();
        root.setCenter(createMiddle());
        writeText();
        removeText();




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
            buttonMap.put(b, i);
            b.setPrefSize(200, screenBounds.getHeight() / 5);
            b.setStyle("-fx-alignment: bottom-right;");

            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
//                    PerformSomeAction();
                }
            });

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
        mergedNode = new Label("Question");
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
}
