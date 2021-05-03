package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;

class CatalogueController implements FxController {
    private MainController mainController;
    private Map<WordList, Label> listLabelMap;
    @FXML
    private Button newListButton;
    @FXML
    private Button learnButton;
    @FXML
    private VBox cataloguePane;

    CatalogueController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        newListButton.setOnAction(event -> VocUtils.getDialogBuilder().openTextFieldDialog("",
                s -> {
                    WordList newWordList = Core.getInstance().catalogueService().createWordList(s);
                    updateCatalogue();
                    listSelectionListener(newWordList);
                }));
        learnButton.setOnAction(event -> {
            List<Entry> entriesToLearn = Core.getInstance().learningService().getEntriesToLearn();
            if (entriesToLearn.isEmpty()) {
                VocUtils.getDialogBuilder().openInfoDialog("No words to learn");
            } else {
                VocabularyWindowBuilder.getInstance().openLearningWindow(new LearningController(entriesToLearn));
            }
        });

        updateCatalogue();
    }

    void updateListsTitles() {
        for (Map.Entry<WordList, Label> mapEntry : listLabelMap.entrySet()) {
            mapEntry.getValue().setText(mapEntry.getKey().getTitle());
        }
    }

    void updateCatalogue() {
        List<WordList> catalogue = Core.getInstance().catalogueService().getCatalogue();
        listLabelMap = new HashMap<>();
        cataloguePane.getChildren().clear();
        for (WordList list : catalogue) {
            Label label = new Label(list.getTitle());
            label.setMinHeight(30);
            label.setOnMouseClicked(event -> listSelectionListener(list));
            listLabelMap.put(list, label);
            cataloguePane.getChildren().addAll(label, getPaneWithHeight(20));
        }
    }

    void listSelectionListener(WordList wordList) {
        mainController.showList(wordList);
        Label label = listLabelMap.get(wordList);
        for (Label label1 : listLabelMap.values()) {
            label1.setStyle(VocUtils.getNotSelectedListStyle());
        }
        label.setStyle(VocUtils.getSelectedListStyle());
    }
}
