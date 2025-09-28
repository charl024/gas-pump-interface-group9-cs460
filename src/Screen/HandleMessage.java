package Screen;

import IOPort.CommPort;
import IOPort.PortLookupMap;
import MessagePassed.Message;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HandleMessage {
    private CommPort port;

    private ScreenDisplay screenDisplay;
    private ScreenDisplay.PossibleActionsForButton possibleActions;
    private Timer timer;

    // TODO //IMPORTANT// Need to change to actual port number and fix this
    ScreenServer server = new ScreenServer(PortLookupMap.PortMap(6), this);

    public HandleMessage(ScreenDisplay screenDisplay) throws IOException {
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
            server.sendMessage(invalidMessage);
        } else {

//            if (parts.length == 2) {
            if (parts[1].equals("INITIALPRICE")) {

                screenDisplay.setPrices(
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]));
                screenDisplay.resetLabels();
                screenDisplay.showWelcomeScreen();
            } else if (parts[1].equals("AUTHORIZING")) {
                screenDisplay.resetLabels();
                screenDisplay.showAuthorizationScreen();


                timeoutTimer();

            } else if (parts[1].equals("VALIDCARD")) {
                cancelTimeout();

                screenDisplay.resetLabels();
                screenDisplay.showCardAcceptedScreen();

                timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        screenDisplay.resetLabels();
                        initiateGasSelection();
                    }
                }, 5000);
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
            } else if (parts[1].equals("PUMPINGPROGRESS")) {
                cancelTimeout();
                screenDisplay.showPumpingProgress();
            } else if (parts[1].equals("HOSEPAUSED")) {
                screenDisplay.resetLabels();
                screenDisplay.showHosePausedScreen();
            } else if (parts[1].equals("NEWTOTAL")) {
                screenDisplay.resetLabels();

                screenDisplay.showFuelFinishedScreen(
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]));

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        screenDisplay.resetLabels();
                        screenDisplay.showWelcomeScreen();
                    }
                }, 10000);
            }else if(parts[1].equals("CHANGEPRICES")){
                screenDisplay.setPrices(
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

    private void initiateGasSelection() {
        screenDisplay.showGasSelectionScreen();
        Message message = new Message();
        screenDisplay.setOnAction(code -> {

            if (code == 0) {
                message.addToDescription("" + screenDisplay.getRegPrice());
                cancelTimeout();

                screenDisplay.resetLabels();
                screenDisplay.showConnectHoseScreen();

                timeoutTimer();
            } else if (code == 1) {
                message.addToDescription("" + screenDisplay.getPlusPrice());
                cancelTimeout();

                screenDisplay.resetLabels();
                screenDisplay.showConnectHoseScreen();

                timeoutTimer();
            } else if (code == 2) {
                message.addToDescription("" + screenDisplay.getPremPrice());
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
        server.sendMessage(message);
        // Might need to write above button listener
        timeoutTimer();
    }
}
