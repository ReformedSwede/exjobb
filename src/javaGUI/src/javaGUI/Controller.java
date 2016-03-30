package javaGUI;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    public Label display;

    public TextField input;

    public void doStuff(){
        display.setText(input.getText());
    }
}
