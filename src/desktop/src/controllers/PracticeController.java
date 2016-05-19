package controllers;

import grammar.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.QRGen;
import main.ResourceManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Model;
import main.SyncThread;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
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
    VBox catCheckList;
    @FXML
    VBox inflectionCheckList;
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
            CheckBox cb = new CheckBox(pos);
            cb.setSelected(true);
            cb.setOnAction(event -> catCheckSelected(cb));
            catCheckList.getChildren().add(cb);
            unusedInflections.put(pos, new HashSet<>());

            updateInflectionChecks(ResourceManager.getInflectionRealNamesByCat(pos), true);
        });

        langToggle.setSelected(translateToNative = true);

        //Start!
        setNextWord();
    }

    /**
     * Displays a new random word to the user
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
     * Checks whether the user's answer is correct
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
            model.getStats().pushStats(currentWord, inflectionForm, correct);

            setNextWord();
        }
    }

    /**
     * Called when the user selects or deselects one of the check boxes for categories
     * @param cb The check box that was clicked
     */
    private void catCheckSelected(CheckBox cb){
        if(cb.isSelected()){
            //A box was selected and thus, a category should be added

            unusedCategories.remove(cb.getText());
            unusedInflections.get(cb.getText()).clear();
            updateInflectionChecks(ResourceManager.getInflectionRealNamesByCat(cb.getText()), true);
        }else{
            //If this was the last check box to be deselected, it must be selected again.
            //Otherwise there would be no categories to generation a random word from!
            if(unusedCategories.size() + 1 == model.getAllCategories().size()) {
                cb.setSelected(true);
                return;
            }

            //A button was deselected and thus, a category should be removed
            List<String> inflections = ResourceManager.getInflectionRealNamesByCat(cb.getText());
            unusedCategories.add(cb.getText());
            unusedInflections.put(cb.getText(), new HashSet<>(inflections));
            updateInflectionChecks(inflections, false);

            setNextWord();
        }
    }

    /**
     * Called when the user selects or deselects one of the boxes for inflections
     * @param cb The box that was clicked
     */
    private void inflectionChecksSelected(CheckBox cb){
        //Find out which part of speech this box belongs to
        String partOspeech = "";
        for(String s : ResourceManager.getPartOfSpeechCats())
            for(String i : ResourceManager.getInflectionRealNamesByCat(s))
                if(cb.getText().equals(i))
                    partOspeech = s;

        //Handle event
        if(cb.isSelected()){
            //A button was selected and thus, a inflection form should be added
            unusedInflections.get(partOspeech).remove(cb.getText());
        }else{
            //A box was deselected and thus, a inflection form should be removed
            //If this was the last box to be deselected, it must be selected again.
            //Otherwise there would be no inflections to generation a random word from!
            if(unusedInflections.get(partOspeech).size() + 1
                    == ResourceManager.getInflectionRealNamesByCat(partOspeech).size()) {
                cb.setSelected(true);
                return;
            }
            unusedInflections.get(partOspeech).add(cb.getText());

            setNextWord();
        }
    }

    /**
     * Update the check boxes in the inflection radio list
     * @param inflections Collection of inflections to either remove or insert
     * @param insert If true, the collection will be inserted, o/w they will be removed
     */
    private void updateInflectionChecks(Collection<String> inflections, boolean insert){
        if(insert){
            //Insert inflections into the list
            inflections.forEach(inflection -> {
                CheckBox cb = new CheckBox(inflection);
                cb.setSelected(true);
                cb.setOnAction(event -> inflectionChecksSelected(cb));
                inflectionCheckList.getChildren().add(cb);
            });
        }else{
            //Filter inflections from the list
            inflectionCheckList.getChildren().setAll(inflectionCheckList.getChildren().stream()
                    .filter(btn -> !inflections.contains(((CheckBox)btn).getText()))
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

    private InetAddress getCurrentIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while(nias.hasMoreElements()) {
                    InetAddress ia= nias.nextElement();
                    if (!ia.isLinkLocalAddress()
                            && !ia.isLoopbackAddress()
                            && ia instanceof Inet4Address) {
                        return ia;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends grammar to android app
     */
    public void synchronize(){
        // Create the custom dialog.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        //Start listening for incoming connections
        SyncThread thread = new SyncThread(alert, model.getAllGrammar());
        thread.startListening();

        //Display QR
        BufferedImage bf = new QRGen().getQR(getCurrentIp().getHostAddress());
        WritableImage wr = null;
        if (bf != null) {
            wr = new WritableImage(bf.getWidth(), bf.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bf.getWidth(); x++) {
                for (int y = 0; y < bf.getHeight(); y++) {
                    pw.setArgb(x, y, bf.getRGB(x, y));
                }
            }
        }
        ImageView view = new ImageView(wr);
        view.setFitHeight(300);
        view.setFitWidth(300);
        alert.setGraphic(view);
        alert.setTitle("Scan");
        alert.setHeaderText(null);
        alert.setContentText("Open the scanner in your app and scan this QR code.");
        alert.getDialogPane().getStylesheets().add("/resources/css/dialog-style.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            thread.stopListening();
        }
    }

    /****Navigation methods****/

    /**
     * Fills the stage content with the start fxml
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
     * Fills the stage content with the statistics fxml
     */
    public void statistics(){
        //Load FXML
        URL url = getClass().getResource("/resources/view/stats-window.fxml");
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
        ((StatisticsController)fxmlloader.getController()).init(model, pContent);
    }

    /**
     * Fills the stage content with the edit fxml
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
