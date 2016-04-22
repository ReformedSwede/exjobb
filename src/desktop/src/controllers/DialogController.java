package controllers;

import grammar.Word;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.InflectionCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogController {
    private boolean editing = false;
    private String originalForeignWord; //used for removing original word. Done in callback.

    private Word word;
    private InflectionCallback callback;
    private Stage thisStage;
    private List<TextField> nativeFields = new ArrayList<>();
    private List<TextField> foreignFields = new ArrayList<>();

    public Label catLbl;
    public VBox nativePanel;
    public VBox foreignPanel;
    public VBox inflectionPanel;
    public Button editBtn;

    public void init(Word word, InflectionCallback callback, Stage thisStage){
        this.word = word;
        this.callback = callback;
        this.thisStage = thisStage;

        catLbl.setText("Category: " + word.getCategory());
        List<String> inflections = word.getAllInflectionNames();
        nativePanel.getChildren().add(new Label(word.getNativeLanguage()));
        foreignPanel.getChildren().add(new Label(word.getForeignLanguage()));
        inflectionPanel.getChildren().add(new Label());
        for(String i : inflections){
            inflectionPanel.getChildren().add(new Label(i));

            TextField tf = new TextField(word.getNativeInflectionFormByName(i));
            tf.setEditable(false);
            nativeFields.add(tf);
            nativePanel.getChildren().add(tf);

            tf = new TextField(word.getForeignInflectionFormByName(i));
            tf.setEditable(false);
            foreignFields.add(tf);
            foreignPanel.getChildren().add(tf);
        }
    }

    public void ok(){
        if(editing)
            callback.call(word.getCategory(), word.getFunction(),
                    nativeFields.stream().map((textField) ->
                            textField.getText().trim()).collect(Collectors.toList()),
                    foreignFields.stream().map((textField1) ->
                            textField1.getText().trim()).collect(Collectors.toList()));
        thisStage.close();
    }

    public void edit(){
        for(TextField tf : nativeFields)
            tf.setEditable(true);
        for(TextField tf : foreignFields)
            tf.setEditable(true);
        editing = true;
        editBtn.setDisable(true);
        originalForeignWord = foreignFields.get(0).getText();
    }
}
