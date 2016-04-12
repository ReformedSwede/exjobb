package ui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Random;


public class Controller {

    public Label foreignWord;

    public TextField input;

    public Label info;

    public ImageView image;

    public void submit(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER)){

            image.setImage(new Image((new Random().nextBoolean()) ? "/resources/x.png" : "/resources/checkmark.png"));
        }
    }
}
