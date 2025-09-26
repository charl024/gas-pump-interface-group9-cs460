import IOPort.CommPort;

import MessagePassed.Message;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {
    private MainController mainController;
    private CommPort gasServerPort;
    private CommPort bankServerPort;
    private CommPort cardReaderPort;

    private ExecutorService executor;

    public ServerManager(MainController mainController) {
        this.mainController = mainController;
        //When called, it should then create the IOConnections that will then
        // connect to the corresponding devices

        //Card reader won't receive any messages, just sends information out

        //Gas station server should receive input for when a gas transaction
        // is finished. Gas station can send out information about the gas
        // prices

        //Bank server will receive input of a credit card, it will determine
        // if its valid or not. After it determines validation, it will send
        // the information back

        gasServerPort = new CommPort(2);
        bankServerPort = new CommPort(1);

        cardReaderPort = new CommPort(3);
        executor = Executors.newFixedThreadPool(3);
        start();
    }

    public void start() {
        executor.submit(() -> listenOnPort(gasServerPort));
        executor.submit(() -> listenOnPort(bankServerPort));
        executor.submit(() -> listenOnPort(cardReaderPort));
    }

    public void handleMessage(Message message) {
        String description = message.getDescription();
        //Going to do this section by devices
        String[] parts = description.split("-");

        //Messages that are sent from the Card Reader:
        if (parts[0].equals("CR")) {
            message.changeDevice("BS");
            sendMessage(bankServerPort, message);
        }
        //Messages that are sent from the BankServer:
        if(parts[0].equals("BS")) {
            //TODO THEN NEED TO SEND A MESSAGE TO
            if(parts[2].equals("INVALIDCARD")) {
                //Inform the screen so that it can change to the correct display
            } else if(parts[2].equals("VALIDCARD")) {

            }
        }

        //Messages that are received are sent by Gas Station server
        if(parts[0].equals("GS")) {
            //Gas station will only be sending information about what the
            // current gas prices are
            if(parts[1].equals("GASINFODONE")) {
                message.changeDevice("SC");
                //then send message to screen with prices
            }
        }
    }

    /**
     * Handles messages that need to be sent to Gas Station Server
     */
    public void messageRequest(Message message) {
        String description = message.getDescription();
        String[] parts = description.split("-");
        if(parts[0].equals("GS")) {
            //Three messages that can be sent to gas station:
            //New total: Value that needs to be added to the server
            //Update gas: New gas prices (i have no clue whos calling this
            //Gas info: Requests the current gas prices
            sendMessage(gasServerPort, message);
        }
    }

    private void sendMessage(CommPort port, Message message) {
        port.send(message);
    }

    private void listenOnPort(CommPort port) {
        while (!Thread.currentThread().isInterrupted()) {
            Message msg = port.get();
            if (msg != null) {
                handleMessage(msg);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public void handleStop() {
        executor.shutdown();
    }
}
