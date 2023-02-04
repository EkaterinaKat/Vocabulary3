package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.NodeInfo.SEARCH_RESULT;

class SearchController implements FxController {
    @FXML
    private TextField textField;
    @FXML
    private Pane searchResultPane;

    @FXML
    private void initialize() {
        SearchResultController searchResultController = new SearchResultController();
        searchResultPane.getChildren().add(VocabularyWindowBuilder.getInstance().getNode(SEARCH_RESULT, searchResultController));
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> searchResultController.fillTable(textField.getText()));
    }
}
