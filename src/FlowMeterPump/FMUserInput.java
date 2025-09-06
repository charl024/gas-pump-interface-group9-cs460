package FlowMeterPump;

public class FMUserInput {
    private final FMDisplay display;

    public FMUserInput(FMDisplay display) {
        this.display = display;
    }

    public void handleStop() {
        if (!display.isTimerRunning()) {
            return; //do nothing if timing isn't running
        } else {
            display.handleStop(); //stop timer
        }
        System.out.println("Stop button clicked");
    }
}
