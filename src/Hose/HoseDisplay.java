package Hose;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class HoseDisplay extends BorderPane {
    private final int displayWidth = 400;
    private final int displayHeight = 400;
    private final int connectorWidth = 40;
    private final int connectorHeight = 20;

    private Rectangle draggableHoseConnector;
    private Rectangle hoseEnd;
    private Rectangle hoseStart;
    private Rectangle hoseHose;
    private Rectangle hoseNozzle;
    private Rectangle hoseEndpoint;
    private Circle endpointCircle;

    private double connectorX;

    private boolean connected = false;

    public HoseDisplay() {
        this.setPrefSize(displayWidth, displayHeight);
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        hoseHoseSetup();
        hoseConnectorSetup();
        hoseNozzleRectangleSetup();
        hoseEndRectangleSetup();
        hoseStartRectangleSetup();
        hoseEndPointRectangleSetup();

        this.getChildren().add(hoseStart);
        this.getChildren().add(hoseEnd);
        this.getChildren().add(hoseEndpoint);
        this.getChildren().add(endpointCircle);
        this.getChildren().add(draggableHoseConnector);
        this.getChildren().add(hoseHose);
        this.getChildren().add(hoseNozzle);
    }

    private void hoseEndPointRectangleSetup() {
        hoseEndpoint = new Rectangle(20, 40);
        hoseEndpoint.setFill(Color.DARKGRAY);
        hoseEndpoint.setX(displayWidth - ((double) displayWidth / 4) + 10);
        hoseEndpoint.setY((double) displayHeight /2 - 20);

        double centerX = hoseEndpoint.getX() + 10;
        double centerY = hoseEndpoint.getY() + hoseEndpoint.getHeight() / 2;
        endpointCircle = new Circle(centerX, centerY, 8, Color.BLACK);
    }



    private void hoseStartRectangleSetup() {
        hoseStart = new Rectangle(15, displayHeight);
        hoseStart.setFill(Color.BLACK);
        hoseStart.setX(0);
    }

    private void hoseNozzleRectangleSetup() {
        hoseNozzle = new Rectangle(20, 5);
        hoseNozzle.setFill(Color.GRAY);
        hoseNozzle.setX(10 + connectorWidth);
        hoseNozzle.setY((double) displayHeight /2 - 2.5);

    }

    private void hoseEndRectangleSetup() {
        hoseEnd = new Rectangle((double) displayWidth /4, displayHeight);
        hoseEnd.setFill(Color.YELLOW);
        hoseEnd.setX(displayWidth - (double) displayWidth / 4);
    }

    private void hoseHoseSetup() {
        hoseHose = new Rectangle(0, (double) connectorHeight /2);
        hoseHose.setFill(Color.DARKBLUE);
        hoseHose.setX(10);
        hoseHose.setY((double) displayHeight /2 - (double) connectorHeight /4);
    }

    private void hoseConnectorSetup() {
        draggableHoseConnector = new Rectangle(connectorWidth, connectorHeight);
        draggableHoseConnector.setArcHeight(10);
        draggableHoseConnector.setArcWidth(10);
        draggableHoseConnector.setStrokeWidth(1);
        draggableHoseConnector.setStroke(Color.BLACK);
        draggableHoseConnector.setFill(Color.LIGHTBLUE);
        draggableHoseConnector.setX(10);
        draggableHoseConnector.setY((double) displayHeight /2 - (double) connectorHeight /2);

        draggableHoseConnector.setOnMousePressed(e -> {
            connectorX = e.getSceneX() - draggableHoseConnector.getX();
            draggableHoseConnector.setOpacity(0.80);
            draggableHoseConnector.setStrokeWidth(5);
        });

        draggableHoseConnector.setOnMouseReleased(e -> {
            draggableHoseConnector.setOpacity(1);
            draggableHoseConnector.setStrokeWidth(1);
        });

        draggableHoseConnector.setOnMouseDragged(e -> {
           double connectorNewX = e.getSceneX() - connectorX;
           connectorNewX = Math.max(10, Math.min(displayWidth - connectorWidth - ((double) displayWidth /4), connectorNewX));
           draggableHoseConnector.setX(connectorNewX);

           hoseNozzle.setX(connectorNewX + 40);

           hoseHose.setX(10);
           hoseHose.setWidth(connectorNewX - 10);

            connected = connectorNewX == (displayWidth - connectorWidth - ((double) displayWidth / 4));
        });
    }

    public boolean isConnected() {
        return connected;
    }
}
