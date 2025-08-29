package FlowMeter;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
//Flow meter tells how much gas has been pumped
//When gas is finished pumping, and a new car starts pumping, reset volume
// count to zero
public class FMDisplay {
    private final BorderPane pane; //Where all images/text will be placed on
    private final Button stopFuel;
    private final Label costInfo;
    private final Label volInfo;
    private HBox info;
    private double cost = 0;
    private double vol = 0;
    private double gasRate;
    private double volRate;
    //Would an actual progress bar be helpful? If so, what is considered
    // being full/how would we know


    public FMDisplay(){
        pane = new BorderPane();
        stopFuel = new Button("Stop");
        costInfo = new Label("Cost: $" + cost);
        volInfo = new Label(vol + " Gallons");
        info = new HBox(costInfo, volInfo);

        pane.setCenter(info);
        pane.setRight(stopFuel);


    }

    //create methods that will update current cost and volumes
//    public static void main(String[] args) {
//        FMDisplay fmDisplay = new FMDisplay();
//
//    }
}
