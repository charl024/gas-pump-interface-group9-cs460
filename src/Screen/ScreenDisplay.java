package Screen;
import javafx.scene.layout.*;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ScreenDisplay {


    private final Pane screenPane;
    public ScreenDisplay() {
        screenPane =  new Pane();

    }

    public Pane getScreenPane() {
        return screenPane;
    }
}
