package Screen;

import IOPort.CommPort;
import MessagePassed.Message;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class HandleMessage {
    private CommPort port;

    private ScreenDisplay screenDisplay;
    private ScreenDisplay.PossibleActionsForButton possibleActions;
    //    private ScheduledExecutorService scheduler  = Executors.newScheduledThreadPool(1);
    private Timer timer;

    public HandleMessage(ScreenDisplay screenDisplay) {
        this.screenDisplay = screenDisplay;
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
            Message invalidMessage = new Message("SC-INVALID");
            sendMessage(invalidMessage);
        } else {

            if (parts.length == 2) {
                // Delete this welcome case
                if (parts[1].equals("WELCOME")) {
                    screenDisplay.showWelcomeScreen();
                } else if (parts[1].equals("AUTHORIZING")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showAuthorizationScreen();


                    timeoutTimer();

                } else if (parts[1].equals("VALIDCARD")) {
                    cancelTimeout();

                    screenDisplay.resetLabels();
                    screenDisplay.showCardAcceptedScreen();

                    //TODO remove if statement for getGas, combine with VALIDCARD

                } else if (parts[1].equals("INVALIDCARD")) {
                    cancelTimeout();

                    screenDisplay.resetLabels();
                    screenDisplay.showCardDeniedScreen();


                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            screenDisplay.resetLabels();
                            screenDisplay.showWelcomeScreen();
                        }
                    }, 10000);
                } else if (parts[1].equals("GASINFO")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showGasSelectionScreen();
                    Message message = new Message();
                    screenDisplay.setOnAction(code -> {

                        if (code == 0) {
                            //TODO send message with gas price instead of the type of gas
                            message.addToDescription("Regular");
                            cancelTimeout();

                            screenDisplay.resetLabels();
                            screenDisplay.showConnectHoseScreen();

                            timeoutTimer();
                        } else if (code == 1) {
                            message.addToDescription("Plus");
                            cancelTimeout();

                            screenDisplay.resetLabels();
                            screenDisplay.showConnectHoseScreen();

                            timeoutTimer();
                        } else if (code == 2) {
                            message.addToDescription("Premium");
                            cancelTimeout();

                            screenDisplay.resetLabels();
                            screenDisplay.showConnectHoseScreen();

                            timeoutTimer();
                        } else if (code == 5) {
                            screenDisplay.resetLabels();
                            screenDisplay.showTransactionCanceledScreen();
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    screenDisplay.resetLabels();
                                    screenDisplay.showWelcomeScreen();
                                }
                            }, 10000);
                        }
                        System.out.println("code being sent for gas " + code);
                    });
                    sendMessage(message);
                    // Might need to write above button listener
                    timeoutTimer();

                } else if (parts[1].equals("PUMPINGPROGRESS")) {
                    cancelTimeout();
                    screenDisplay.showPumpingProgress();
                } else if (parts[1].equals("HOSEPAUSED")) {
                    screenDisplay.resetLabels();
                    screenDisplay.showHosePausedScreen();
                } else if (parts[1].equals("NEWTOTAL")) {
                    screenDisplay.resetLabels();

                    // Example values, replace with actual pumping results
                    // Needs to get this number from a message somewhere
                    double totalGallons = 10.23;
                    double totalPrice = 35.87;

                    screenDisplay.showFuelFinishedScreen(totalGallons, totalPrice);


                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            screenDisplay.resetLabels();
                            screenDisplay.showWelcomeScreen();
                        }
                    }, 10000);
                }
//                else if (parts[1].equals("PUMPUNAVAILABLE")) {
//                    screenDisplay.resetLabels();
//                    screenDisplay.showPumpUnavailableScreen();
//                }
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

    private void timeoutTimer() {
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                screenDisplay.resetLabels();
                screenDisplay.showTimeoutScreen();
            }
        }, 60000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                screenDisplay.resetLabels();
                screenDisplay.showWelcomeScreen();
            }
        }, 70000);
    }

    private void cancelTimeout() {
        if (timer != null) {
            timer.cancel(); // cancels all scheduled tasks
            timer = null;
        }
    }

    public void sendMessage(Message msg) {
        port.send(msg);
    }
}
