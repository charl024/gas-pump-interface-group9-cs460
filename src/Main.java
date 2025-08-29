import IOPort.IOPort;
import Screen.Screen;

class Main {
    public static void main(String[] args) {
        /*
          this is where we do our testing
          create and connect stuff here maybe (for testing)?
         */


        IOPort screenPort = new IOPort(5050);

        screenPort.send("test!");

        System.out.println(screenPort.read());
        System.out.println(screenPort.get());

        System.out.println("Hello world!");
    }
}