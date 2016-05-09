package controllers;

import grammar.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.ResourceManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller class for the practice frame.
 * Contains methods for displaying words and comparing the user's answer, as well as handling selection of inflections.
 */
public class PracticeController{

    private Model model;
    private Word currentWord, prevWord = null; //Used in random word generation
    private String inflectionForm; //Used in random word generation
    private Set<String> unusedCategories = new HashSet<>(); //List of categories not to use in random word generation
    private HashMap<String, Set<String>> unusedInflections = new HashMap<>(); //Inflections not to use in random word generation
    private boolean translateToNative = true; //Determines if the user is to translate the given word to the native lang

    //UI components
    private Pane pContent;
    @FXML
    Label sessionTitle;
    @FXML
    Label practiceWordLbl;
    @FXML
    TextField inputFld;
    @FXML
    Label infoLbl;
    @FXML
    ImageView imageView;
    @FXML
    VBox catRadioList;
    @FXML
    VBox inflectionRadioList;
    @FXML
    ToggleButton langToggle;

    /**
     * Initialized the view
     * @param model The current model in use
     * @param content The JavaFX pane of the view
     */
    void init(Model model, Pane content){
        this.model = model;
        this.pContent = content;

        //Init window
        pContent.getScene().getWindow().setHeight(700);
        pContent.getScene().getWindow().setWidth(1200);

        //Init session title
        sessionTitle.setText("Native language: " + ResourceManager.codeToName(model.getNativeLangCode()) +
                ", foreign language: " + ResourceManager.codeToName(model.getForeignLangCode()));

        //Init radio buttons
        ResourceManager.getPartOfSpeechCats().forEach(pos -> {
            RadioButton rb = new RadioButton(pos);
            rb.setSelected(true);
            rb.setOnAction(event -> catRadioSelected(rb));
            catRadioList.getChildren().add(rb);
            unusedInflections.put(pos, new HashSet<>());

            updateInflectionRadios(ResourceManager.getInflectionRealNamesByCat(pos), true);
        });

        langToggle.setSelected(translateToNative = true);

        //Start!
        setNextWord();
    }

    /**
     * Called to display a new random word to the user
     */
    private void setNextWord(){
        inputFld.clear();

        //Generate a word, distinct from the previous one
        do{
            currentWord = model.getRandomWord(unusedCategories.toArray(new String[0]));
        }while(currentWord.equals(prevWord) && model.getNrOfWords() > 1);
        prevWord = currentWord;
        inflectionForm = currentWord.getRandomInflectionName(unusedInflections.get(currentWord.getCategory()));

        //Update the view
        practiceWordLbl.setText(currentWord.getWordInflectionFormByName(inflectionForm, !translateToNative));
    }

    /**
     * Called when the user hits the enter key in the input field
     * @param e The key event that was triggered
     */
    public void submit(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER)){
            //Check if answer was correct
            boolean correct;
            correct = currentWord.checkAnswer(inputFld.getText().trim(), inflectionForm, translateToNative);

            //Handle both cases
            if(correct){
                imageView.setImage(new Image("/resources/images/checkmark.png"));
                infoLbl.setText("Correct! \"" + practiceWordLbl.getText() +
                        "\" translates to \"" + inputFld.getText() + "\".");
            }else {
                imageView.setImage(new Image("/resources/images/x.png"));
                infoLbl.setText("Incorrect! \"" + practiceWordLbl.getText() +
                        "\" does not translate to  \"" + inputFld.getText() + "\".\n" + "The correct answer was \"" +
                        currentWord.getWordInflectionFormByName(inflectionForm, translateToNative) + "\".");
            }
            setNextWord();
        }
    }

    /**
     * Called when the user selects or deselects one of the radios for categories
     * @param rb The radio button that was clicked
     */
    private void catRadioSelected(RadioButton rb){
        if(rb.isSelected()){
            //A button was selected and thus, a category should be added

            unusedCategories.remove(rb.getText());
            unusedInflections.get(rb.getText()).clear();
            updateInflectionRadios(ResourceManager.getInflectionRealNamesByCat(rb.getText()), true);
        }else{
            //If this was the last radio button to be deselected, it must be selected again.
            //Otherwise there would be no categories to generation a random word from!
            if(unusedCategories.size() + 1 == model.getAllCategories().size()) {
                rb.setSelected(true);
                return;
            }

            //A button was deselected and thus, a category should be removed
            List<String> inflections = ResourceManager.getInflectionRealNamesByCat(rb.getText());
            unusedCategories.add(rb.getText());
            unusedInflections.put(rb.getText(), new HashSet<>(inflections));
            updateInflectionRadios(inflections, false);

            setNextWord();
        }
    }

    /**
     * Called when the user selects or deselects one of the radios for inflections
     * @param rb The radio button that was clicked
     */
    private void inflectionRadioSelected(RadioButton rb){
        //Find out which part of speech this radio button belongs to
        String partOspeech = "";
        for(String s : ResourceManager.getPartOfSpeechCats())
            for(String i : ResourceManager.getInflectionRealNamesByCat(s))
                if(rb.getText().equals(i))
                    partOspeech = s;

        //Handle event
        if(rb.isSelected()){
            //A button was selected and thus, a inflection form should be added
            unusedInflections.get(partOspeech).remove(rb.getText());
        }else{
            //A button was deselected and thus, a inflection form should be removed
            //If this was the last radio button to be deselected, it must be selected again.
            //Otherwise there would be no inflections to generation a random word from!
            if(unusedInflections.get(partOspeech).size() + 1
                    == ResourceManager.getInflectionRealNamesByCat(partOspeech).size()) {
                rb.setSelected(true);
                return;
            }
            unusedInflections.get(partOspeech).add(rb.getText());

            setNextWord();
        }
    }

    /**
     * Update the radio buttons in the inflection radio list
     * @param inflections Collection of inflections to either remove or insert
     * @param insert If true, the collection will be inserted, o/w they will be removed
     */
    private void updateInflectionRadios(Collection<String> inflections, boolean insert){
        if(insert){
            //Insert inflections into the list
            inflections.forEach(inflection -> {
                RadioButton rbtn = new RadioButton(inflection);
                rbtn.setSelected(true);
                rbtn.setOnAction(event -> inflectionRadioSelected(rbtn));
                inflectionRadioList.getChildren().add(rbtn);
            });
        }else{
            //Filter inflections from the list
            inflectionRadioList.getChildren().setAll(inflectionRadioList.getChildren().stream()
                    .filter(btn -> !inflections.contains(((RadioButton)btn).getText()))
                    .collect(Collectors.toList()));
        }
    }

    /**
     * Called when the user clicks the language toggle button
     */
    public void toggleLanguage(){
        translateToNative = langToggle.isSelected();
        langToggle.setText(translateToNative ? "Translate to native" : "Translate to foreign");
        setNextWord();
    }


    /****Navigation methods****/

    /**
     * Called when the exit button is clicked
     */
    public void exit(){
        model.endSession();

        //Load FXML of the start frame
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

        //Init controller
        ((StartController)fxmlloader.getController()).initialize(null, null);
    }

    /**
     * Called when switching to the edit frame
     */
    public void editSession(){
        //Load FXML
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

        //Init edit controller
        ((EditController)fxmlloader.getController()).init(model, pContent);
    }
}
