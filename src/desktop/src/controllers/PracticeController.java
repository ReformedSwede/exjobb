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
import java.util.*;

public class PracticeController{

    private Model model;
    private Word currentWord, prevWord = null;
    private String inflectionForm;
    private HashMap<String, Set<String>> unusedCategoriesAndInflections = new HashMap<>();
    private boolean translateToNative = true;

    //UI components
    Pane pContent;
    public Label sessionTitle;
    public Label practiceWordLbl;
    public TextField inputFld;
    public Label infoLbl;
    public ImageView imageView;
    public VBox catRadioList;
    public VBox inflectionRadioList;
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

        Utils.getPartOfSpeechCats().forEach(pos -> {
            RadioButton rb = new RadioButton(pos);
            rb.setSelected(true);
            rb.setOnAction(event -> catRadioSelected(rb));
            catRadioList.getChildren().add(rb);

            Utils.getInflectionRealNamesByCat(pos).forEach(inflection -> {
                RadioButton rbtn = new RadioButton(inflection);
                rbtn.setSelected(true);
                rbtn.setOnAction(event -> inflectionRadioSelected(rbtn));
                inflectionRadioList.getChildren().add(rbtn);
            });
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
        if(rb.isSelected()){ //A button was selected and thus, a category was added
            unusedCategoriesAndInflections.remove(rb.getText());
        }else{ //A button was deselected and thus, a category was removed
            if(unusedCategoriesAndInflections.size() + 1 == model.getAllCategories().size()) {
                rb.setSelected(true);
                return;
            }
            List<String> inflections = Utils.getInflectionRealNamesByCat(rb.getText());
            unusedCategoriesAndInflections.put(rb.getText(), new HashSet<>(inflections));

            setNextWord();
        }
    }

    private void inflectionRadioSelected(RadioButton rb){
        //Find out which part of speech this radiobutton belongs to
        String pos = "";
        for(String s : Utils.getPartOfSpeechCats())
            for(String i : Utils.getInflectionRealNamesByCat(s))
                if(rb.getText().equals(i))
                    pos = s;

        if(rb.isSelected()){ //A button was selected and thus, a inflection form was added
            unusedCategoriesAndInflections.get(pos).remove(rb.getText());
        }else{ //A button was deselected and thus, a inflection form was removed
            if(unusedCategoriesAndInflections.get(pos) != null &&
                unusedCategoriesAndInflections.get(pos).size() + 1 == Utils.getInflectionRealNamesByCat(pos).size()) {
                rb.setSelected(true);
                return;
            }
            unusedCategoriesAndInflections.putIfAbsent(pos, new HashSet<>());
            unusedCategoriesAndInflections.get(pos).add(rb.getText());

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
            currentWord = model.getRandomWord(unusedCategoriesAndInflections.keySet().toArray(new String[0]));
        }while(currentWord.equals(prevWord));
        prevWord = currentWord;

        inflectionForm = currentWord.getRandomInflectionName(
                unusedCategoriesAndInflections.get(currentWord.getCategory()));
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
