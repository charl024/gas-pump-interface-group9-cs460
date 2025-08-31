package FlowMeter;

public class UserInputFM {
    private FMDisplay display;
    public UserInputFM(FMDisplay display){
        this.display = display;
    }
    public void handleStop(){
        if (!display.isTimerRunning()){
            return; //do nothing if timing isn't running
        } else {
            display.handleStop(); //stop timer
        }
        System.out.println("Stop button");
        //If this is clicked, on then we need to stop the timer
    }
}
