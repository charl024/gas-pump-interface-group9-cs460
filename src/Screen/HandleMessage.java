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

    // TODO //IMPORTANT// Need to change to actual port number and fix this
    private ScreenServer server;

    public HandleMessage(ScreenDisplay screenDisplay) throws IOException {
        this.screenDisplay = screenDisplay;
        server = new ScreenServer(PortLookupMap.PortMap(6), this);
        new Thread(server).start();
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

//            if (parts.length == 2) {
            if (parts[1].equals("INITIALPRICE")) {

                screenDisplay.setPrices(
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]));
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showWelcomeScreen();
                });

            } else if (parts[1].equals("AUTHORIZING")) {
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showAuthorizationScreen();
                });


                timeoutTimer();

            } else if (parts.length >= 3 && parts[2].equals("VALIDCARD")) {
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
                            });

                        }
                    }, 5000);
                });

//                timer = new Timer();
//
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        screenDisplay.resetLabels();
//                        initiateGasSelection();
//                    }
//                }, 5000);
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
                            });
                        }
                    }, 10000);
                });


            } else if (parts[1].equals("PUMPINGPROGRESS")) {
                cancelTimeout();
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showPumpingProgress();
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
            } else if (parts[1].equals("NEWTOTAL")) {
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();

                    screenDisplay.showFuelFinishedScreen(
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]));
                });


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
            } else if (parts[1].equals("CHANGEPRICES")) {
                screenDisplay.updateGasPrices(
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]));
            }


//                else if (parts[1].equals("PUMPUNAVAILABLE")) {
//                    screenDisplay.resetLabels();
//                    screenDisplay.showPumpUnavailableScreen();
//                }
//            }

            //MAY NEED TO DELETE THIS CODE, ASK KYLE

//            if (parts.length == 3) {
//                screenDisplay.writeText(parts[1], Integer.parseInt(parts[2]));
//                return;
//            }
//            if (!parts[1].equals("*")) {
//                String[] s = parts[1].split("\\.");
//                screenDisplay.changeFont(s[0], Integer.parseInt(s[1]));
//            }
//            if (!parts[2].equals("*")) {
//                String[] s = parts[2].split("\\.");
//                screenDisplay.changeTextSize(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
//            }
//            if (!parts[3].equals("*")) {
//                // may remove this block of code
//                //Start
//                String[] s = parts[3].split("\\.");
//                screenDisplay.changeButtonColor(
//                        screenDisplay.convertColor(s[0]),
//                        Integer.parseInt(s[1])
//                );
//                //End
//                screenDisplay.changeButtonColorV2(
//                        screenDisplay.convertColorV2(s[0]),
//                        Integer.parseInt(s[1])
//                );
//            }
//            if (!parts[4].equals("*")) {
//                String[] s = parts[4].split("\\.");
//                screenDisplay.giveButtonAction(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
//            }

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
}
