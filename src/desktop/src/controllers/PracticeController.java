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
    private Word currentWord;
    private String inflectionForm;
    private Set<String> unusedPartsOfSpeech = new HashSet<>();

    //UI components
    Pane pContent;
    public Label sessionTitle;
    public Label foreignWordLbl;
    public TextField inputFld;
    public Label infoLbl;
    public ImageView imageView;
    public Button exit;
    public VBox posRadioList;


    public void init(Model model, Pane content){
        this.model = model;
        this.pContent = content;

        //Init window
        pContent.getScene().getWindow().setHeight(700);
        pContent.getScene().getWindow().setWidth(1000);

        //Init components
        sessionTitle.setText("Current session: " + Utils.codeToName(model.getNativeLangCode()) +
                " speaker practising " + Utils.codeToName(model.getForeignLangCode()));

        model.getAllPartOfSpeech().forEach(s -> {
            RadioButton rb = new RadioButton(s);
            rb.setSelected(true);
            rb.setOnAction(event -> posRadioSelected(rb));
            posRadioList.getChildren().add(rb);
        });

        //Start!
        setNextWord();
    }

    public void submit(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER)){
            //grade answer
            boolean correct;
            if(inflectionForm == null)
                correct = currentWord.checkAnswer(inputFld.getText().trim());
            else
                correct = currentWord.checkInflectedAnswer(inputFld.getText().trim(), inflectionForm);

            if(correct){
                imageView.setImage(new Image("/resources/images/checkmark.png"));
                infoLbl.setText("Correct! \"" + foreignWordLbl.getText() + "\" is \"" + inputFld.getText() + "\".");
            }else{
                imageView.setImage(new Image("/resources/images/x.png"));
                infoLbl.setText("Incorrect! \"" + foreignWordLbl.getText() +
                    "\" is not \"" + inputFld.getText() + "\".\n" + "The correct answer was \"" +
                    (inflectionForm == null ?
                            currentWord.getNative() : currentWord.getNativeInflectionFormByName(inflectionForm)) +
                        "\".");
            }
            setNextWord();
        }
    }

    private void posRadioSelected(RadioButton rb){
        if(rb.isSelected()){
            unusedPartsOfSpeech.remove(rb.getText());
        }else{
            if(unusedPartsOfSpeech.size() + 1 == model.getAllPartOfSpeech().size()) {
                rb.setSelected(true);
                return;
            }
            unusedPartsOfSpeech.add(rb.getText());
        }
    }

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

    private void setNextWord(){
        inputFld.clear();
        if((currentWord = model.getRandomWord(unusedPartsOfSpeech.toArray(new String[0]))).hasInflections()) {
            inflectionForm = currentWord.getRandomInflectionName();
                    foreignWordLbl.setText(currentWord.getForeignInflectionFormByName(inflectionForm));
        }else {
            foreignWordLbl.setText(currentWord.getForeign());
            inflectionForm = null;
        }
    }
}
