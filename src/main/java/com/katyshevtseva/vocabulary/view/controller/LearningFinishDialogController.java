package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.service.LearningService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class LearningFinishDialogController implements FxController {
    private final List<Entry> problematicEntries;
    @FXML
    private Label statisticsLabel;
    @FXML
    private Label problematicEntriesLabel;
    @FXML
    private Button okButton;
    @FXML
    private Button copyButton;

    LearningFinishDialogController(List<Entry> problematicEntries) {
        this.problematicEntries = problematicEntries;
    }

    @FXML
    private void initialize() {
        statisticsLabel.setText(LearningService.getStatisticsReport());
        okButton.setOnAction(event -> FxUtils.closeWindowThatContains(okButton));

        String pel = getProblematicEntriesLog();
        problematicEntriesLabel.setText(pel);
        copyButton.setOnAction(event -> GeneralUtils.saveToClipBoard(pel));
    }

    private String getProblematicEntriesLog() {
        StringBuilder result = new StringBuilder();
        for (Entry entry : problematicEntries.stream()
                .sorted(Comparator.comparing(Entry::getWordList).thenComparing(Entry::getCreationDate))
                .collect(Collectors.toList())) {
            result.append(String.format("%s : %s : %s : %s \n",
                    entry.getWord(), entry.getTranslation(), entry.getWordList(), entry.getPage()));
        }
        return result.toString();
    }
}
