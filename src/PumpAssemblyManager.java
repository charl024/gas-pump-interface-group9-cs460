import IOPort.CommPort;
import IOPort.IOPort;
import IOPort.StatusPort;
import Util.Message;
import Util.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Pump Assembly Manager, handles Hose and Flowmeter/Pump
 */
public class PumpAssemblyManager implements Manager {
    private final StatusPort hosePort;
    private final CommPort flowMeterPumpPort;

    private boolean hoseConnected = false;
    private boolean pumping = false;
    private boolean startedPumping = false;
    private boolean priceSelected = false;

    public PumpAssemblyManager() {
        hosePort = new StatusPort(5);
        flowMeterPumpPort = new CommPort(4);
    }

    @Override
    public List<IOPort> getPorts() {
        return List.of(hosePort, flowMeterPumpPort);
    }

    @Override
    public List<Message> handleMessage(Message message) {
        System.out.printf("[PumpAssemblyManager] Received message: %s%n", message.getDescription());

        List<Message> toForward = new ArrayList<>();
        String[] parts = message.getDescription().split("-");
        String device = parts[0];

        switch (device) {
            case "HS" -> handleHoseMessage(parts, toForward);
            case "FM" -> handleFlowMeterMessage(parts, message, toForward);
        }

        return toForward;
    }

    private void handleHoseMessage(String[] parts, List<Message> toForward) {
        String hoseStatus = parts[1];
        hoseConnected = hoseStatus.equals("CN");
        System.out.printf("[PumpAssemblyManager] Hose status: %s (connected=%s)%n", hoseStatus, hoseConnected);

        if (priceSelected && hoseConnected && !pumping) {
            sendStartPump();
            toForward.add(new Message("SC-PUMPINGPROGRESS"));
        } else {
            if (pumping && !hoseConnected) {
                pumping = false;
                flowMeterPumpPort.send(new Message("FM-PAUSE"));
                System.out.println("[PumpAssemblyManager] Pump paused due to hose disconnect");
                toForward.add(new Message("SC-HOSEPAUSED"));
            }
            if (hoseConnected && !pumping && startedPumping) {
                pumping = true;
                flowMeterPumpPort.send(new Message("FM-RESUME"));
                System.out.println("[PumpAssemblyManager] Pump resumed after hose reconnect");
                toForward.add(new Message("SC-PUMPINGPROGRESS"));
            }
        }
    }

    private void handleFlowMeterMessage(String[] parts, Message message, List<Message> toForward) {
        String flowMeterInfo = parts[1];
        System.out.printf("[PumpAssemblyManager] FlowMeter message: %s%n", flowMeterInfo);

        if (flowMeterInfo.equals("NEWTOTAL")) {
            startedPumping = false;
            priceSelected = false;
            System.out.println("[PumpAssemblyManager] Pumping complete. Resetting state.");

            Message toScreen = new Message(message.getDescription());
            toScreen.changeDevice("SC");
            toForward.add(toScreen);

            Message toServer = new Message(message.getDescription());
            toServer.changeDevice("GS");
            toForward.add(toServer);
        }
    }

    @Override
    public void sendMessage(Message message) {
        System.out.printf("[PumpAssemblyManager] Sending to %s: %s%n",
                message.getDescription().split("-")[0],
                message.getDescription());

        String[] parts = message.getDescription().split("-");
        if (parts[0].equals("FM")) {
            if (parts[1].equals("GASSELECTION")) {
                priceSelected = true;
                System.out.println("[PumpAssemblyManager] Gas selection received, price set.");
            }
            flowMeterPumpPort.send(message);
        }
    }

    private void sendStartPump() {
        Message startPump = new Message("FM-START");
        pumping = true;
        startedPumping = true;
        flowMeterPumpPort.send(startPump);
        System.out.println("[PumpAssemblyManager] Pump started.");
    }
}
