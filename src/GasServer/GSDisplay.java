/**
 * Gas Station Server Display, handles the GUI for Gas Station
 */
package GasServer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Gas Station Display
 */
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

    /**
     * Gas Station Display Constructor, creates all the panes and texts
     */
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

    /**
     * Update the total that the gas station has made
     */
    public void updateTotal() {
        String format = String.format("Total Money: $%.2f", total);
        totalMoney.setText(format);
    }

    /**
     * Update the price for regular gas
     */
    private void updateRegular() {
        String format = String.format("$%.2f", regularCost);
        regularCostLabel.setText(format);
    }

    /**
     * Update the price for plus gas
     */
    private void updatePlus() {
        String format = String.format("$%.2f", plusCost);
        plusCostLabel.setText(format);
    }

    /**
     * Update the price for premium gas
     */
    private void updatePremium() {
        String format = String.format("$%.2f", premiumCost);
        premiumCostLabel.setText(format);
    }

    public void setDemoPrices() {
        total = 133.20;
        regularCost = 2.89;
        plusCost = 3.32;
        premiumCost = 3.77;
        updateGasPrices();
        updateTotal();
    }

    /**
     * Update the gas prices for all three types
     */
    public void updateGasPrices() {
        updateRegular();
        updatePlus();
        updatePremium();
    }

    /**
     * Return main Pane that holds everything
     *
     * @return Main Pane
     */
    public BorderPane getPane() {
        return pane;
    }

    /**
     * Get the current amount of money that the gas station has made
     *
     * @return Money made by the gas station
     */
    public double getTotal() {
        return total;
    }

    /**
     * Set the new total that the gas station has made
     *
     * @param total New total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Get the current cost of regular gas
     *
     * @return Regular gas cost
     */
    public double getRegularCost() {
        return regularCost;
    }

    /**
     * Set the new price for regular gas
     *
     * @param regularCost New regular gas cost
     */
    public void setRegularCost(double regularCost) {
        this.regularCost = regularCost;
    }

    /**
     * Get the current cost of plus gas
     *
     * @return Plus gas cost
     */
    public double getPlusCost() {
        return plusCost;
    }

    /**
     * Set the new price for plus gas
     *
     * @param plusCost New plug gas cost
     */
    public void setPlusCost(double plusCost) {
        this.plusCost = plusCost;
    }

    /**
     * Get the current cost of premium gas
     *
     * @return Premium gas cost
     */
    public double getPremiumCost() {
        return premiumCost;
    }

    /**
     * Set the new price for premium gas
     *
     * @param premiumCost New premium gas cost
     */
    public void setPremiumCost(double premiumCost) {
        this.premiumCost = premiumCost;
    }
}
