package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MainController implements FxController {
    private ListController listController = new ListController(this);
    private CatalogueController catalogueController = new CatalogueController(this);
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
        logoImageView.setImage(new Image("/images/logo.png"));
        cataloguePane.getChildren().add(VocabularyWindowBuilder.getInstance().getCatalogueNode(catalogueController));
        searchPane.getChildren().add(VocabularyWindowBuilder.getInstance().getSearchNode(new SearchController()));
        listPane.getChildren().add(VocabularyWindowBuilder.getInstance().getListNode(listController));
    }

    void showList(WordList wordList) {
        listController.showWordListIfItIsNotNull(wordList);
    }

    void updateListsTitlesInCatalogue() {
        catalogueController.updateListsTitles();
    }

    void updateCatalogue() {
        catalogueController.updateCatalogue();
    }

    void catalogueListSelectionListener(WordList wordList) {
        catalogueController.listSelectionListener(wordList);
    }

}
