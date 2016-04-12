package ui;

import grammar.GrammarManager;
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
        Collection<String> c = new GrammarManager("Swedish", "English").getAllLanguages();
        for(String s : c)
                languages.setText(languages.getText() + s);
    }

    public void insert(){
        String text = input.getText();
        test.setText(text);
        new GrammarManager("Swedish", "English").addWord("Eng", "Kind", text);
    }
}
