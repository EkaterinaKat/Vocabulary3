package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class MainController implements FxController {
    @FXML
    private Pane cataloguePane;
    @FXML
    private Pane searchPane;
    @FXML
    private Pane listPane;

    @FXML
    private void initialize() {
        cataloguePane.getChildren().add(VocabularyWindowBuilder.getInstance().getCatalogueNode(new CatalogueController()));
        searchPane.getChildren().add(VocabularyWindowBuilder.getInstance().getSearchNode(new SearchController()));
        listPane.getChildren().add(VocabularyWindowBuilder.getInstance().getListNode(new ListController()));
    }


}
