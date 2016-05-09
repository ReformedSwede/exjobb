package controllers;

import grammar.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.InflectionCallback;
import main.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class fot the confirm dialog. This dialog window appears whenever the user adds a word to the database.
 */
public class DialogController {
    private boolean editing = false;

    private Word word;
    private InflectionCallback callback;
    private Stage thisStage;
    private List<TextField> nativeFields = new ArrayList<>();
    private List<TextField> foreignFields = new ArrayList<>();

    @FXML
    Label catLbl;
    @FXML
    GridPane table;
    @FXML
    Button editBtn;

    /**
     * Initializes the dialog view.
     * @param word The word to display
     * @param callback The method to call when the user hits the OK button
     * @param thisStage The JavaFX stage of the dialog.
     */
    void init(Word word, InflectionCallback callback, Stage thisStage){
        this.word = word;
        this.callback = callback;
        this.thisStage = thisStage;

        //Init labels & add to view
        catLbl.setText("Category: " + word.getCategory());
        Label labelPtr = new Label(word.getNativeLanguage());
        GridPane.setConstraints(labelPtr, 1, 0);
        table.getChildren().add(labelPtr);
        labelPtr = new Label(word.getForeignLanguage());
        GridPane.setConstraints(labelPtr, 2, 0);
        table.getChildren().add(labelPtr);

        //Add data to table
        int row = 1;
        for(String i : ResourceManager.getInflectionRealNamesByCat(word.getCategory())){
            //Add label with inflection name
            labelPtr = new Label(i);
            GridPane.setConstraints(labelPtr, 0, row);
            table.getChildren().add(labelPtr);

            //Add label with native inflection
            TextField tf = new TextField(word.getNativeInflectionFormByName(i));
            tf.setEditable(false);
            nativeFields.add(tf);
            GridPane.setConstraints(tf, 1, row);
            table.getChildren().add(tf);

            //Add label with foreign inflection
            tf = new TextField(word.getForeignInflectionFormByName(i));
            tf.setEditable(false);
            foreignFields.add(tf);
            GridPane.setConstraints(tf, 2, row);
            table.getChildren().add(tf);
            row++;
        }
    }

    /**
     * Is called whenever the user hits the OK button.
     * Submits the form and closes the window.
     */
    public void ok(){
        if(editing)
            callback.call(word.getCategory(), word.getFunction(),
                    nativeFields.stream().map((textField) ->
                            textField.getText().trim()).collect(Collectors.toList()),
                    foreignFields.stream().map((textField1) ->
                            textField1.getText().trim()).collect(Collectors.toList()));
        thisStage.close();
    }

    /**
     * Is called whenever the user hits the EDIT button.
     * Enables the user to edit the form
     */
    public void edit(){
        for(TextField tf : nativeFields)
            tf.setEditable(true);
        for(TextField tf : foreignFields)
            tf.setEditable(true);
        editing = true;
        editBtn.setDisable(true);
    }
}
