package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import main.Model;
import main.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

public class EditController {

    private Model model;

    //UI components
    public Pane pContent;
    public Label sessionTitle;
    public TilePane posList;
    public TilePane wordList;
    public ImageView exit;

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

    public void init(Model model, Pane content){
        this.model = model;
        pContent = content;
        pContent.getScene().getWindow().setHeight(700);
        pContent.getScene().getWindow().setWidth(900);

        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            exitSession();
            event.consume();
        });

        sessionTitle.setText("Current session: " + Utils.codeToName(model.getNativeLangCode()) +
                " speaker practising " + Utils.codeToName(model.getForeignLangCode()));
        model.getAllPartOfSpeech().forEach(pos -> posList.getChildren().add(new Label(pos)));
        model.getAllForeignWords().forEach(word -> wordList.getChildren().add(new Label(word)));
    }

}
