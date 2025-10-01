package Screen;

import Util.PortLookupMap;
import Util.Message;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HandleMessage {
    private ScreenDisplay screenDisplay;
    private ScreenDisplay.PossibleActionsForButton possibleActions;
    private Timer timer;

    private ScreenServer server;

    private boolean onScreenUnavailable;
    private boolean onWelcomeScreen;
    private boolean onAuthorizing;
    private boolean onGasSelection;
    private boolean onPumping;
    private boolean onWaitingConnection;

    public HandleMessage(ScreenDisplay screenDisplay) throws IOException {
        this.screenDisplay = screenDisplay;
        server = new ScreenServer(PortLookupMap.PortMap(6), this);
        new Thread(server).start();
        onScreenUnavailable = true;
        onWelcomeScreen = false;
        onAuthorizing = false;
        onGasSelection = false;
        onPumping = false;
        onWaitingConnection = false;
    }

    /*        String message examples
    //        This message is used to change things like
    //        the font style, font size, button color, given button an action, give label some text
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
     */

    public void handleMessage(Message msg) {


        String messageStr = msg.getDescription();

        String[] parts = messageStr.split("-");

        String deviceType = parts[0];
        if (!(deviceType.equals("SC"))) {
            Message invalidMessage = new Message("SC-INVALID");
            server.sendMessage(invalidMessage);
        } else {
            if (onScreenUnavailable) {
                if (parts[1].equals("INITIALPRICE")) {

                    screenDisplay.setPrices(
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            Double.parseDouble(parts[4]));
                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showWelcomeScreen();
                    });
                    onScreenUnavailable = false;
                    onWelcomeScreen = true;
                }
            } else if (onWelcomeScreen) {
                if (parts[1].equals("AUTHORIZING")) {
                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showAuthorizationScreen();
                    });


                    timeoutTimer();
                }
                onWelcomeScreen = false;
                onAuthorizing = true;
            } else if (onAuthorizing) {
                if (parts.length >= 3 && parts[2].equals("VALIDCARD")) {
                    cancelTimeout();

                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showCardAcceptedScreen();
                        timer = new Timer();

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    screenDisplay.resetLabels();
                                    initiateGasSelection();
                                    onGasSelection = true;
                                    onAuthorizing = false;
                                });

                            }
                        }, 5000);
                    });
                } else if (parts.length >= 3 && parts[2].equals("INVALIDCARD")) {

                    cancelTimeout();

                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showCardDeniedScreen();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    screenDisplay.resetLabels();
                                    screenDisplay.showWelcomeScreen();
                                    onWelcomeScreen = true;
                                    onAuthorizing = false;
                                });
                            }
                        }, 10000);
                    });
                }
            } else if (onGasSelection) {
                if (parts[1].equals("PUMPINGPROGRESS")) {
                    //display screen that says "hose is pumping"
                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showPumpingProgress();
                        onPumping = true;
                        onGasSelection = false;
                    });
                } else if (parts[1].equals("DC")) {
                    //display screen that says "please connect hose"
                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showConnectHoseScreen();
                        onWaitingConnection = true;
                        onGasSelection = false;
                    });
                }
            } else if (onWaitingConnection) {
                if (parts[1].equals("PUMPINGPROGRESS")) {
                    cancelTimeout();
                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showPumpingProgress();
                        onWaitingConnection = false;
                        onPumping = true;
                    });
                }
            } else if (onPumping) {
                if (parts[1].equals("PUMPINGPROGRESS")) {
                    cancelTimeout();
                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showPumpingProgress();
                        onWaitingConnection = false;
                    });
                } else if (parts[1].equals("HOSEPAUSED")) {
                    Platform.runLater(() -> {
                        screenDisplay.resetLabels();
                        screenDisplay.showHosePausedScreen();
                    });

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                screenDisplay.resetLabels();
                                screenDisplay.showTimeoutScreen();
                                sendServerMessage(new Message("SC-NEWTOTAL"));
                            });

                        }
                    }, 10000);
                }
            }

            //these messages won't be randomly sent out like the others could
            if (parts[1].equals("NEWTOTAL")) {
                Platform.runLater(() -> {
                    resetVars();
                    screenDisplay.resetLabels();
                    double price = Double.parseDouble(parts[2]);
                    double volume = Double.parseDouble(parts[3]);
                    screenDisplay.showFuelFinishedScreen(price, volume);
                });

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            screenDisplay.resetLabels();
                            screenDisplay.showWelcomeScreen();
                            onWelcomeScreen = true;
                        });
                    }
                }, 10000);
            } else if (parts[1].equals("CHANGEPRICES")) {
                double reg = Double.parseDouble(parts[2]);
                double plus = Double.parseDouble(parts[3]);
                double prem = Double.parseDouble(parts[4]);
                screenDisplay.updateGasPrices(reg, plus, prem);
            }
        }
    }

    public void timeoutTimer() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showTimeoutScreen();
                    sendServerMessage(new Message("SC-CANCELTRANSACTION"));
                    System.out.println("Timeout triggered!"); // debug message
                });
            }
        }, 60000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showWelcomeScreen();
                    System.out.println("Back to welcome screen"); // debug message
                });
            }
        }, 70000);
    }

    public void cancelTimeout() {
        if (timer != null) {
            timer.cancel(); // cancels all scheduled tasks
            timer = null;
        }
    }

    public void transactionCancel() {
        screenDisplay.resetLabels();
        screenDisplay.showTransactionCanceledScreen();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showWelcomeScreen();
                });
            }
        }, 10000);
    }

    private void initiateGasSelection() {

        screenDisplay.showGasSelectionScreen();
        screenDisplay.setOnGasSelection();
        timeoutTimer();
    }

    public void sendServerMessage(Message msg) {
        server.sendMessage(msg);
    }

    public void setWelcomeScreen() {
        onWelcomeScreen = true;
    }

    public void resetVars() {
        onAuthorizing = false;
        onGasSelection = false;
        onPumping = false;
        onWaitingConnection = false;
    }
}
