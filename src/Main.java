import IOPort.CommPort;
import MessagePassed.Message;

class Main {
    public static void main(String[] args) {
        // main from Main

        CommPort screenPort1 = new CommPort(5050);

        Message message = new Message("What's up from Main");
        screenPort1.send(message);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Message reply = screenPort1.get();
        System.out.println("Main 1 Received: " + reply.getDescription());

//        // main from Screen
//
//        CommPort screenPort2 = new CommPort(5050);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Message message = screenPort2.get();
//        System.out.println("Screen received: " + message.getDescription());
//        screenPort2.send(new Message("Hello from Screen"));
    }
}