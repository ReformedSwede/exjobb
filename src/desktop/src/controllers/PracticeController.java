package controllers;

import grammar.Word;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Utils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class PracticeController{

    private Model model;
    private Word currentWord, prevWord = null;
    private String inflectionForm;
    private Set<String> unusedCategories = new HashSet<>();
    private boolean translateToNative = true;

    //UI components
    Pane pContent;
    public Label sessionTitle;
    public Label practiceWordLbl;
    public TextField inputFld;
    public Label infoLbl;
    public ImageView imageView;
    public Button exit;
    public VBox catRadioList;
    public ToggleButton langToggle;


    public void init(Model model, Pane content){
        this.model = model;
        this.pContent = content;

        //Init window
        pContent.getScene().getWindow().setHeight(700);
        pContent.getScene().getWindow().setWidth(1200);

        //Init components
        sessionTitle.setText("Native language: " + Utils.codeToName(model.getNativeLangCode()) +
                ", foreign language: " + Utils.codeToName(model.getForeignLangCode()));

        Utils.getPartOfSpeechCats().forEach(s -> {
            RadioButton rb = new RadioButton(s);
            rb.setSelected(true);
            rb.setOnAction(event -> catRadioSelected(rb));
            catRadioList.getChildren().add(rb);
        });

        langToggle.setSelected(translateToNative = true);

        //Start!
        setNextWord();
    }

    public void submit(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER)){
            //grade answer
            boolean correct;
            correct = currentWord.checkAnswer(inputFld.getText().trim(), inflectionForm, translateToNative);

            if(correct){
                imageView.setImage(new Image("/resources/images/checkmark.png"));
                infoLbl.setText("Correct! \"" + practiceWordLbl.getText() +
                        "\" translates to \"" + inputFld.getText() + "\".");
            }else{
                imageView.setImage(new Image("/resources/images/x.png"));
                infoLbl.setText("Incorrect! \"" + practiceWordLbl.getText() +
                    "\" does not translate to  \"" + inputFld.getText() + "\".\n" + "The correct answer was \"" +
                         currentWord.getWordInflectionFormByName(inflectionForm, translateToNative) + "\".");
            }
            setNextWord();
        }
    }

    private void catRadioSelected(RadioButton rb){
        if(rb.isSelected()){
            unusedCategories.remove(rb.getText());
        }else{
            if(unusedCategories.size() + 1 == model.getAllCategories().size()) {
                rb.setSelected(true);
                return;
            }
            unusedCategories.add(rb.getText());
            setNextWord();
        }
    }

    public void toggleLanguage(){
        langToggle.setText(langToggle.isSelected() ? "Translate to native" : "Translate to foreign");
        translateToNative = langToggle.isSelected();
        setNextWord();
    }

    private void setNextWord(){
        inputFld.clear();
        do{
            currentWord = model.getRandomWord(unusedCategories.toArray(new String[0]));
        }while(currentWord.equals(prevWord));
        prevWord = currentWord;

        inflectionForm = currentWord.getRandomInflectionName();
        practiceWordLbl.setText(currentWord.getWordInflectionFormByName(inflectionForm, !translateToNative));
    }


    /****Navigation methods****/

    public void exit(){
        model.endSession();
        URL url = getClass().getResource("/resources/view/start-window.fxml");

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(url);
        fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());

        pContent.getChildren().clear();
        try {
            pContent.getChildren().add(fxmlloader.load(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((StartController)fxmlloader.getController()).initialize(null, null);
    }

    public void editSession(){
        URL url = getClass().getResource("/resources/view/edit-window.fxml");

        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(url);
        fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());

        pContent.getChildren().clear();
        try {
            pContent.getChildren().add(fxmlloader.load(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((EditController)fxmlloader.getController()).init(model, pContent);
    }
}
