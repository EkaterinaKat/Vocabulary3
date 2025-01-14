package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.core.service.CatalogueService;
import com.katyshevtseva.vocabulary.core.service.CatalogueService.ListStatus;
import com.katyshevtseva.vocabulary.core.service.LearningService;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.DialogInfo.*;

class CatalogueController implements FxController {
    private final MainController mainController;
    private Map<WordList, Label> listLabelMap;
    @FXML
    private Button newListButton;
    @FXML
    private Button learnButton;
    @FXML
    private Button frequentSectionButton;
    @FXML
    private VBox cataloguePane;
    @FXML
    private ComboBox<ListStatus> listStatusBox;
    @FXML
    private Button daysReportButton;

    CatalogueController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItems(listStatusBox, ListStatus.values(), ListStatus.ACTIVE);
        listStatusBox.setOnAction(event -> updateCatalogue());

        DcTextField titleField = new DcTextField(true, "");
        newListButton.setOnAction(event -> DialogConstructor.constructDialog(() -> {
            WordList newWordList = CatalogueService.createWordList(titleField.getValue());
            updateCatalogue();
            listSelectionListener(newWordList);
        }, titleField));

        learnButton.setOnAction(event -> {
            List<Entry> entriesToLearn = LearningService.getEntriesToLearn();
            if (entriesToLearn.isEmpty()) {
                new StandardDialogBuilder().openInfoDialog("No words to learn");
            } else {
                VocabularyWindowBuilder.getInstance().openDialog(LEARNING,
                        new LearningController(entriesToLearn, mainController::updateStatistics));
            }
        });
        frequentSectionButton.setOnAction(event -> VocabularyWindowBuilder.getInstance().openDialog(FREQUENT_SECTION,
                new FrequentSectionController()));
        daysReportButton.setOnMouseClicked(event -> {
            VocabularyWindowBuilder.getInstance().openDialog(DAYS_REPORT, new DaysReportController());
        });

        updateCatalogue();
    }

    void updateListsTitles() {
        for (Map.Entry<WordList, Label> mapEntry : listLabelMap.entrySet()) {
            mapEntry.getValue().setText(mapEntry.getKey().getTitle());
        }
    }

    void updateCatalogue() {
        List<WordList> catalogue = CatalogueService.getCatalogue(listStatusBox.getValue());
        listLabelMap = new HashMap<>();
        cataloguePane.getChildren().clear();
        for (WordList list : catalogue) {
            String frozenMark = list.getFrozen() ? " ❆❆❆ " : "";
            Label label = new Label(frozenMark + list.getTitle());
            label.setMinHeight(30);
            label.setWrapText(true);
            label.setMaxWidth(330);
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
