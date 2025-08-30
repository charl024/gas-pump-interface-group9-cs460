package FlowMeter;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

//Flow meter tells how much gas has been pumped
//When gas is finished pumping, and a new car starts pumping, reset volume
// count to zero
public class FMDisplay {
    private final BorderPane pane; //Where all images/text will be placed on
    private final UserInputFM userInputFM;
    private final Button stopFuel;
    private final Label costInfo;
    private final Label volInfo;
    private VBox info;
    private double cost = 0;
    private double vol = 0;
    private double gasRate;
    private double volRate;
    //Would an actual progress bar be helpful? If so, what is considered
    // being full/how would we know


    public FMDisplay() {
        pane = new BorderPane();
        pane.setMinSize(400, 200);
        userInputFM = new UserInputFM(this);

        //pane.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        stopFuel = new Button("Stop");
        stopFuel.setFocusTraversable(false);
        stopFuel.setOnAction(event -> userInputFM.handleStop());

        costInfo = new Label("Cost: $" + cost);
        costInfo.setFont(new Font(20));
        volInfo = new Label(vol + " Gallons");
        volInfo.setFont(new Font(20));
        info = new VBox(costInfo, volInfo);

        pane.setCenter(info);
        pane.setRight(stopFuel);
        BorderPane.setAlignment(stopFuel, Pos.CENTER);
        info.setAlignment(Pos.CENTER);

    }

    public BorderPane getPane() {
        return pane;
    }
    private void updateValues(){
        //use a time that gets updated every miliseconds?

    }
    //create methods that will update current cost and volumes

}
