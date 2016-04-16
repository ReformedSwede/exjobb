package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Utils;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.Model;

import java.io.IOException;
import java.net.URL;

public class PracticeController{

    private Model model;

    //UI components
    public Label sessionTitle;
    public Label foreignWordLbl;
    public TextField inputFld;
    public Label infoLbl;
    public ImageView imageView;
    public ImageView exit;
    Pane pContent;

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
            foreignWordLbl.setText(model.getRandomWord());
        }
    }

    public void init(Model model, Pane content){
        this.model = model;
        this.pContent = content;
        pContent.getScene().getWindow().setHeight(700);
        pContent.getScene().getWindow().setWidth(900);

        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            exitSession();
            event.consume();
        });

        sessionTitle.setText("Current session: " + Utils.codeToName(model.getNativeLangCode()) +
                " speaker practising " + Utils.codeToName(model.getForeignLangCode()));



        foreignWordLbl.setText(model.getRandomWord());
    }

    public void exitSession(){
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
