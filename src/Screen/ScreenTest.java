//package Screen;
//
//import MessagePassed.Message;
//import javafx.application.Application;
//import javafx.stage.Stage;
//
//public class ScreenTest extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        Screen screen = new Screen();   // Use your Screen class
//        screen.start(primaryStage);     // Initialize everything
//
//        // Fake messages for each screen
//        Message[] testMessages = {
//                new Message("SC-welcome"),
//                new Message("SC-accepted"),
//                new Message("SC-denied"),
//                new Message("SC-gas"),
//                new Message("SC-fuelFinished"),
//                new Message("SC-PumpUnavailable"),
//                new Message ("SC-Disconnected"), // test
//                new Message("SC-receipt"),
//                new Message("SC-Here is some example text.0") // test writing text
//        };
//
//        // Small thread to feed the messages in sequence
//        new Thread(() -> {
//            try {
//                for (Message msg : testMessages) {
//                    Thread.sleep(2000); // wait 2 seconds between screens
//                    System.out.println("Sending: " + msg.getDescription());
//                    screenTestHandle(screen, msg);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    // helper to call private handleMessage (since it's private in Screen)
//    private void screenTestHandle(Screen screen, Message msg) {
//        try {
//            java.lang.reflect.Method m = Screen.class.getDeclaredMethod("handleMessage", Message.class);
//            m.setAccessible(true);
//            m.invoke(screen, msg);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
