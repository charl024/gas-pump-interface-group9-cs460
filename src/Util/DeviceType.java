package Util;

public enum DeviceType {
    SC,  // Screen
    GS,  // Gas Server
    BS,  // Bank Server
    CR,  // Card Reader
    FM,  // Flow Meter / Pump
    HS;  // Hose

    public static DeviceType fromMessage(Message msg) {
        String prefix = msg.getDescription().split("-")[0];
        return DeviceType.valueOf(prefix);
    }
}
