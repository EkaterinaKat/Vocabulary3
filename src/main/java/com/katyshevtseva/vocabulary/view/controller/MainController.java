package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MainController implements FxController {
    private ListController listController = new ListController(this);
    @FXML
    private Pane cataloguePane;
    @FXML
    private Pane searchPane;
    @FXML
    private Pane listPane;
    @FXML
    private ImageView logoImageView;

    @FXML
    private void initialize() {
        logoImageView.setImage(new Image(VocUtils.getLogoImagePath()));
        cataloguePane.getChildren().add(VocabularyWindowBuilder.getInstance().getCatalogueNode(new CatalogueController(this)));
        searchPane.getChildren().add(VocabularyWindowBuilder.getInstance().getSearchNode(new SearchController()));
        listPane.getChildren().add(VocabularyWindowBuilder.getInstance().getListNode(listController));
    }

    void showList(WordList wordList) {
        listController.showWordList(wordList);
    }

}
