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

    private boolean pumping = false;

    // TODO //IMPORTANT// Need to change to actual port number and fix this
    private ScreenServer server;

    public HandleMessage(ScreenDisplay screenDisplay) throws IOException {
        this.screenDisplay = screenDisplay;
        server = new ScreenServer(PortLookupMap.PortMap(6), this);
        new Thread(server).start();
    }

    public void handleMessage(Message msg) {
        String messageStr = msg.getDescription();
        String[] parts = messageStr.split("-");

        String deviceType = parts[0];
        if (!(deviceType.equals("SC"))) {
            Message invalidMessage = new Message("SC-INVALID");
            server.sendMessage(invalidMessage);
            return;
        }

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
                scheduleSingleTask(() -> {
                    screenDisplay.resetLabels();
                    initiateGasSelection();
                }, 5000);
            });

        } else if (parts.length >= 3 && parts[2].equals("INVALIDCARD")) {
            cancelTimeout();
            Platform.runLater(() -> {
                screenDisplay.resetLabels();
                screenDisplay.showCardDeniedScreen();
                scheduleSingleTask(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showWelcomeScreen();
                }, 10000);
            });

        } else if (parts[1].equals("PUMPINGPROGRESS")) {
            pumping = true;
            cancelTimeout();
            Platform.runLater(() -> {
                screenDisplay.resetLabels();
                screenDisplay.showPumpingProgress();
            });

        } else if (parts[1].equals("HOSEPAUSED")) {
            // if not pumping, return (dont do anything)
            if (!pumping) return;
            Platform.runLater(() -> {
                screenDisplay.resetLabels();
                screenDisplay.showHosePausedScreen();
            });
            scheduleSingleTask(() -> {
                if (!pumping) return;
                screenDisplay.resetLabels();
                screenDisplay.showTimeoutScreen();
                sendServerMessage(new Message("SC-NEWTOTAL"));
            }, 10000);

        } else if (parts[1].equals("NEWTOTAL")) {
            pumping = false;
            Platform.runLater(() -> {
                screenDisplay.resetLabels();
                screenDisplay.showFuelFinishedScreen(
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]));
            });
            scheduleSingleTask(() -> {
                screenDisplay.resetLabels();
                screenDisplay.showWelcomeScreen();
            }, 10000);

        } else if (parts[1].equals("CHANGEPRICES")) {
            Platform.runLater(() -> {
                screenDisplay.updateGasPrices(
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]));
            });
        }
    }

    public void timeoutTimer() {
        if (pumping) return;
        // prevent duplicate timers
        cancelTimeout();
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pumping) return;
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showTimeoutScreen();
                    System.out.println("Timeout triggered!");
                });
            }
        }, 60000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pumping) return;
                Platform.runLater(() -> {
                    screenDisplay.resetLabels();
                    screenDisplay.showWelcomeScreen();
                    System.out.println("Back to welcome screen");
                });
            }
        }, 70000);
    }

    public void cancelTimeout() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void scheduleSingleTask(Runnable action, long delayMs) {
        // run a single delayed task without interfering with timeout timers.
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (pumping) return; // safeguard
                Platform.runLater(action);
            }
        }, delayMs);
    }

    public void transactionCancel() {
        pumping = false;
        cancelTimeout();
        Platform.runLater(() -> {
            screenDisplay.resetLabels();
            screenDisplay.showTransactionCanceledScreen();
        });
        scheduleSingleTask(() -> {
            screenDisplay.resetLabels();
            screenDisplay.showWelcomeScreen();
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
