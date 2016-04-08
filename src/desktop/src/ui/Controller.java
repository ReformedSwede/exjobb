package ui;

import grammar.PGFInterface;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Collection;

public class Controller {

    public Label languages;

    public Label test;

    public TextField input;

    public Button insert;

    public Button display;

    public void display(){
        Collection<String> c = PGFInterface.getAllLanguages();
        for(String s : c)
                languages.setText(languages.getText() + s);
    }

    public void insert(){
        String text = input.getText();
        test.setText(text);
        PGFInterface.addWord("Eng", "Kind", text);
    }
}