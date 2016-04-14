package controllers;

import main.Utils;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Model;

public class PracticeController{

    private Model model;

    //UI components
    public Label sessionTitle;
    public Label foreignWordLbl;
    public TextField inputFld;
    public Label infoLbl;
    public ImageView imageView;

    public void submit(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER)){

            //grade answer
            boolean correct = model.gradeAnswer(foreignWordLbl.getText(), inputFld.getText().trim());

            if(correct){
                imageView.setImage(new Image("/resources/images/checkmark.png"));
                infoLbl.setText("Correct! \"" + foreignWordLbl.getText() + "\" is \"" + inputFld.getText() + "\".");
            }else{
                imageView.setImage(new Image("/resources/images/x.png"));
                infoLbl.setText("Incorrect! \"" + foreignWordLbl.getText() +
                        "\" is not \"" + inputFld.getText() + "\".");
            }
            inputFld.clear();
        }
    }

    public void init(Model model){
        this.model = model;
        //foreignWordLbl.setText(model.getRandomWord());

        sessionTitle.setText("Current session: " + Utils.codeToName(model.getNativeLangCode()) +
                " -> " + Utils.codeToName(model.getForeignLangCode()));
    }

    public void exitSession(){

    }
}
