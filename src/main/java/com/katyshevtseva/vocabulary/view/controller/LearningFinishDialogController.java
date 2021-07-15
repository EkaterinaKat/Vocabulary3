package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class LearningFinishDialogController implements FxController {
    private List<Entry> problematicEntries;
    @FXML
    private Label statisticsLabel;
    @FXML
    private Label problematicEntriesLabel;
    @FXML
    private Button okButton;

    LearningFinishDialogController(List<Entry> problematicEntries) {
        this.problematicEntries = problematicEntries;
    }

    @FXML
    private void initialize() {
        statisticsLabel.setText(Core.getInstance().learningService().getStatisticsReport());
        problematicEntriesLabel.setText(getProblematicEntriesLog());
        okButton.setOnAction(event -> FxUtils.closeWindowThatContains(okButton));
    }

    private String getProblematicEntriesLog() {
        System.out.println("getProblematicWordsLog");
        StringBuilder result = new StringBuilder();
        for (Entry entry : problematicEntries.stream()
                .sorted(Comparator.comparing(Entry::getWordList).thenComparing(Entry::getCreationDate))
                .collect(Collectors.toList())) {
            result.append(String.format("%s : %s : %s : %s\n",
                    entry.getWord(), entry.getTranslation(), entry.getWordList(), entry.getPage()));
        }
        return result.toString();
    }
}
