import IOPort.CommPort;
import MessagePassed.Message;

class Main {
    public static void main(String[] args) {
        // main from Main

        CommPort screenPort1 = new CommPort(1);

        Message message = new Message("What's up from Main");
        screenPort1.send(message);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Message reply = screenPort1.get();
        System.out.println("Main 1 Received: " + reply.getDescription());

        //TESTING CODE FOR FLOW METER:

        CommPort flowIOPort = new CommPort(2);
        //Message that we want to send to the Flow Meter
        Message flowMessage = new Message("FM-2.86-10-15");
        flowIOPort.send(flowMessage); //Idk if the flow should send a message
        // back saying indicating that it received the message correctly

        //TESTING CODE FOR FLOW METER ENDING



//        // main from Screen
//
//        CommPort screenPort2 = new CommPort(1);
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