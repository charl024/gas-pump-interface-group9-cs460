import IOPort.IOPort;
import MessagePassed.Message;

class Main {
    public static void main(String[] args) {
        // main from Main

        IOPort screenPort1 = new IOPort(5050);

        Message message = new Message("What's up from Main");
        screenPort1.send(message);

        Message reply = screenPort1.get();
        System.out.println("Main 1 Received: " + reply.getDescription());

//        // main from Screen
//
//        IOPort screenPort2 = new IOPort(5050);
//
//        Message message = screenPort2.get();
//        System.out.println("Screen received: " + message.getDescription());
//        screenPort2.send(new Message("Hello from Screen"));
    }
}