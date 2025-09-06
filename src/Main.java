import IOPort.CommPort;
import MessagePassed.Message;

import java.util.Random;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // main from Main

        //Have to comment this out to test flow meter sorry
        // its okay - Charles
//        CommPort screenPort1 = new CommPort(1);
//
//        Message message = new Message("What's up from Main");
//        screenPort1.send(message);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Message reply = screenPort1.get();
//        System.out.println("Main 1 Received: " + reply.getDescription());

        //TESTING CODE FOR FLOW METER:

        CommPort flowIOPort = new CommPort(2);
//        //Message that we want to send to the Flow Meter
//        Message flowMessage = new Message("FM-2.86-10-15");
//        flowIOPort.send(flowMessage); //Idk if the flow should send a message
//        System.out.println("testing here");
//        // back saying indicating that it received the message correctly
//
//        //TESTING CODE FOR FLOW METER ENDING
//
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Is Flow Meter finished? Enter YES to move on to next device");
//        boolean move = false;
//        while (!move) {
//            String input = scan.nextLine();
//            if (input.toUpperCase().equals("YES")) {
//                move = true;
//            }
//        }


        //TESTING CODE FOR CARD READER:
//        CommPort cardIOPort = new CommPort(3);
//        Message cardMessage = new Message("CR");
//        cardIOPort.send(cardMessage);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Message newMessage = cardIOPort.get();
//        String stringMessage = newMessage.toString();
//        if (stringMessage.equals("CR-TAPPED")) {
//            //send a message back to the ioport saying to indicate if the card is valid or not. Valid or not can be done through a random number
//            Random rand = new Random();
//            int randomVal = (int) (Math.random() * 10) + 1; //one through ten
//            if (randomVal == 1) {
//                Message valid = new Message("CR-VALID");
//                cardIOPort.send(valid);
//            } else {
//                Message invalid = new Message("CR-INVALID");
//                cardIOPort.send(invalid);
//            }
//        }
        //TESTING CODE FOR CARD READER ENDING

        //TESTING CODE FOR BANK SERVER
//        CommPort bankIOPort = new CommPort(4);
//        Message bankMessage = new Message("BS-12312398");
//        bankIOPort.send(bankMessage);

        //TESTING CODE FOR BANK SERVER ENDING

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