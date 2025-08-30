package FlowMeter;

public class UserInputFM {
    private FMDisplay display;
    public UserInputFM(FMDisplay display){
        this.display = display;


    }
    public void handleStop(){
        System.out.println("Stop button");
    }
}
