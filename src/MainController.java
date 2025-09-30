import IOPort.*;
import Util.*;

import java.util.*;

public class MainController {
    private final ServerManager serverManager;
    private final ScreenManager screenManager;
    private final PumpAssemblyManager pumpAssembly;

    private final Map<DeviceType, Manager> routingTable = new HashMap<>();
    private final List<Manager> managers = new ArrayList<>();
    private final Map<IOPort, String> lastMessages = new HashMap<>();

    public MainController() {
        pumpAssembly = new PumpAssemblyManager();
        serverManager = new ServerManager();
        screenManager = new ScreenManager();

        registerManager(pumpAssembly, List.of(DeviceType.FM, DeviceType.HS));
        registerManager(serverManager, List.of(DeviceType.GS, DeviceType.BS, DeviceType.CR));
        registerManager(screenManager, List.of(DeviceType.SC));
    }

    private void registerManager(Manager manager, List<DeviceType> prefixes) {
        managers.add(manager);
        for (DeviceType prefix : prefixes) {
            routingTable.put(prefix, manager);
        }
    }

    public void start() {
        Thread dispatchThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                for (Manager m : managers) {
                    for (IOPort port : m.getPorts()) {
                        Message msg = readFromPort(port);
                        if (msg != null) {
                            System.out.printf("[MainController] Received message from port (%s): %s%n",
                                    port.getClass().getSimpleName(), msg.getDescription()
                            );
                            routeMessage(msg, m);
                        }
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        dispatchThread.start();
    }

    private Message readFromPort(IOPort port) {
        Message msg = null;
        if (port instanceof StatusPort s) {
            msg = s.read();
        } else if (port instanceof CommPort c) {
            msg = c.get();
        }

        if (msg != null) {
            String last = lastMessages.get(port);
            if (last != null && last.equals(msg.getDescription())) {
                return null; // prevents duplicate messages
            }
            lastMessages.put(port, msg.getDescription());
        }
        return msg;
    }

    private void routeMessage(Message msg, Manager source) {
        List<Message> forwards = source.handleMessage(msg);

        for (Message fwd : forwards) {
            DeviceType targetPrefix = DeviceType.fromMessage(fwd);
            Manager target = routingTable.get(targetPrefix);
            if (target != null) {
                System.out.printf(
                        "[MainController] Routing message: %s to %s%n",
                        fwd.getDescription(), target.getClass().getSimpleName()
                );
                target.sendMessage(fwd);
            }
        }
    }

    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.start();
        System.out.println("[MainController] Started main dispatch loop.");
    }
}
