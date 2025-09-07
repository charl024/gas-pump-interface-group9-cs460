package GasServer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class GSDisplay {
    private final BorderPane pane;

    private final Label totalMoney; //Total money made by the gas station
    private double total = 0;

    private final Label regularCostLabel;
    private double regularCost;

    private final Label plusCostLabel;
    private double plusCost;

    private final Label premiumCostLabel;
    private double premiumCost;

    public GSDisplay() {
        pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,
                CornerRadii.EMPTY, Insets.EMPTY
        )));


        Label gasStationTitle = new Label("Gas Station");
        gasStationTitle.setStyle("-fx-font-weight: bold");
        gasStationTitle.setFont(new Font(25));
        totalMoney = new Label("Total Money: $" + total);
        totalMoney.setFont(new Font(15));
        //Gas type label
        Label gasTypes = new Label("Gas Types");

        //Holds title, money made, and gas type label
        VBox generalInfo = new VBox(gasStationTitle, totalMoney, gasTypes);
        generalInfo.setSpacing(5);
        generalInfo.setAlignment(Pos.CENTER);


        Label regularLabel = new Label("Regular");
        regularLabel.setStyle("-fx-font-weight: bold");

        Label plusLabel = new Label("Plus");
        plusLabel.setStyle("-fx-font-weight: bold");

        Label premiumLabel = new Label("Premium");
        premiumLabel.setStyle("-fx-font-weight: bold");


        regularCostLabel = new Label();
        plusCostLabel = new Label();
        premiumCostLabel = new Label();


        //Gas type one: Regular
        VBox gasTypeOne = new VBox(regularLabel, regularCostLabel);
        gasTypeOne.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        gasTypeOne.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        //Gas type two: Plus
        VBox gasTypeTwo = new VBox(plusLabel, plusCostLabel);
        gasTypeTwo.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        gasTypeTwo.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        //Gas type three: Premium
        VBox gasTypeThree = new VBox(premiumLabel, premiumCostLabel);
        gasTypeThree.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        gasTypeThree.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        gasTypeOne.setAlignment(Pos.CENTER);
        gasTypeTwo.setAlignment(Pos.CENTER);
        gasTypeThree.setAlignment(Pos.CENTER);

        gasTypeOne.setPrefSize(125, 140);
        gasTypeTwo.setPrefSize(125, 140);
        gasTypeThree.setPrefSize(125, 140);

        //holds the three types of gas types
        HBox gasInfo = new HBox();
        gasInfo.setSpacing(20);
        gasInfo.getChildren().addAll(gasTypeOne, gasTypeTwo, gasTypeThree);

        updateGasPrices();

        pane.setTop(generalInfo);
        pane.setCenter(gasInfo);
    }

    public void updateTotal() {
        String format = String.format("Total Money: $%.2f", total);
        totalMoney.setText(format);
    }

    private void updateRegular() {
        String format = String.format("$%.2f", regularCost);
        regularCostLabel.setText(format);
    }

    private void updatePlus() {
        String format = String.format("$%.2f", plusCost);
        plusCostLabel.setText(format);
    }

    private void updatePremium() {
        String format = String.format("$%.2f", premiumCost);
        premiumCostLabel.setText(format);
    }

    public void updateGasPrices() {
        updateRegular();
        updatePlus();
        updatePremium();
    }

    public BorderPane getPane() {
        return pane;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getRegularCost() {
        return regularCost;
    }

    public void setRegularCost(double regularCost) {
        this.regularCost = regularCost;
    }

    public double getPlusCost() {
        return plusCost;
    }

    public void setPlusCost(double plusCost) {
        this.plusCost = plusCost;
    }

    public double getPremiumCost() {
        return premiumCost;
    }

    public void setPremiumCost(double premiumCost) {
        this.premiumCost = premiumCost;
    }
}
