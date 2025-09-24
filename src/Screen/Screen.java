package Screen;

import IOPort.CommPort;
import MessagePassed.Message;
import javafx.application.Application;
import javafx.stage.Stage;

public class Screen extends Application {
    private CommPort port;

    private ScreenDisplay screenDisplay;
    private ScreenDisplay.PossibleActionsForButton possibleActions;

    public Screen() {

    }

//        String message examples
//        This message is used to change things like
//the font style, font size, button color, given button an action, give label some text
//        messageStr = "SC-i.4-12.4-re.9-3.6";
//        This message is used to add text to a desired label (0)
//        messageStr = "SC-Here is some example text.0";
//        This message initiates welcome screen
//        messageStr = "SC-welcome";
//        This message lets us know if card was accepted
//        messageStr = "SC-accepted";
//        This message lets us know if card was denied
//        messageStr = "SC-denied";
//        This message initiates gas option screen
//        messageStr = "SC-gas";
//        This message shows receipt screen
//        messageStr = "SC-receipt";

    public void handleMessage(Message msg) {
        String messageStr = msg.getDescription();


        String[] parts = messageStr.split("-");

        String deviceType = parts[0];
        if (!(deviceType.equals("SC"))) {
            Message invalidMessage = new Message("SC-Invalid");
            sendMessage(invalidMessage);
        } else {

            if (parts.length == 2) {
                if (parts[1].equals("welcome")) {
                    screenDisplay.showWelcomeScreen();
                } else if (parts[1].equals("authorizing")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showAuthorizationScreen();
                } else if (parts[1].equals("accepted")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showCardAcceptedScreen();
                } else if (parts[1].equals("denied")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showCardDeniedScreen();
                } else if (parts[1].equals("gas")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showGasSelectionScreen();
                    Message message = new Message();
                    screenDisplay.setOnAction(code -> {

                        if (code == 0) {
                            message.addToDescription("Regular");
                        } else if (code == 1) {
                            message.addToDescription("Plus");
                        } else if (code == 2) {
                            message.addToDescription("Premium");
                        }
                    });
                    sendMessage(message);
                } else if (parts[1].equals("connectHose")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showConnectHoseScreen();
                } else if (parts[1].equals("hosePaused")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showHosePausedScreen();
                } else if (parts[1].equals("fuelFinished")) {
                    screenDisplay.resetLabels();

                    // Example values, replace with actual pumping results
                    // Needs to get this number from a message somewhere
                    double totalGallons = 10.23;
                    double totalPrice = 35.87;

                    screenDisplay.showFuelFinishedScreen(totalGallons, totalPrice);

                } else if (parts[1].equals("pumpUnavailable")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showPumpUnavailableScreen();
                } else if (parts[1].equals("Disconnected")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showDisconnectScreen();
                }else if (parts[1].equals("receipt")) {
                    screenDisplay.resetLabels();
                    //screenDisplay.showReceiptScreen();
                    Message message = new Message();
                    screenDisplay.setOnAction(code -> {

                        if (code == 3) {
                            message.addToDescription("Accepted");
                        } else if (code == 4) {
                            message.addToDescription("Denied");
                        }
                    });
                    sendMessage(message);
                }
                return;
            }
            if (parts.length == 3) {
                screenDisplay.writeText(parts[1], Integer.parseInt(parts[2]));
                return;
            }
            if (!parts[1].equals("*")) {
                String[] s = parts[1].split("\\.");
                screenDisplay.changeFont(s[0], Integer.parseInt(s[1]));
            }
            if (!parts[2].equals("*")) {
                String[] s = parts[2].split("\\.");
                screenDisplay.changeTextSize(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
            }
            if (!parts[3].equals("*")) {
                // may remove this block of code
                //Start
                String[] s = parts[3].split("\\.");
                screenDisplay.changeButtonColor(
                        screenDisplay.convertColor(s[0]),
                        Integer.parseInt(s[1])
                );
                //End
                screenDisplay.changeButtonColorV2(
                        screenDisplay.convertColorV2(s[0]),
                        Integer.parseInt(s[1])
                );
            }
            if (!parts[4].equals("*")) {
                String[] s = parts[4].split("\\.");
                screenDisplay.giveButtonAction(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        screenDisplay = new ScreenDisplay();
        screenDisplay.showScreen(primaryStage);

//        this.port = new CommPort(1);

        // thread that polls port for a message
//        new Thread(() -> {
//            try {
//                while (true) {
//                    Thread.sleep(10); // wait 10ms
//                    Message message = port.get();
//                    if (message != null) {
//                        handleMessage(message);
//                    }
//                }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//
//        primaryStage.setOnCloseRequest(e -> {
//            port.close();
//        });

        primaryStage.show();
    }

    public void sendMessage(Message msg) {
        port.send(msg);

    }
}
