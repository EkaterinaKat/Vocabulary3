package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;
import static com.katyshevtseva.fx.FxUtils.setImageOnButton;

class LearningController implements FxController {
    private List<Entry> entries;
    private int wordCount;
    @FXML
    private Label wordLabel;
    @FXML
    private Label translationLabel;
    @FXML
    private Label countLabel;
    @FXML
    private Button showTranslationButton;
    @FXML
    private Label levelLabel;
    @FXML
    private Button okButton;
    @FXML
    private Button notOkButton;
    @FXML
    private Label levelsInfoLabel;

    LearningController(List<Entry> entries) {
        this.entries = entries;
    }

    @FXML
    private void initialize() {
        setImageOnButton("images/tick.png", okButton, 25);
        setImageOnButton("images/red_cross.png", notOkButton, 25);
        okButton.setOnAction(event -> resultButtonsListener(true));
        notOkButton.setOnAction(event -> resultButtonsListener(false));
        showTranslationButton.setOnAction(event -> showTranslationButtonListener());
        wordCount = -1;
        nextWord();
        setLevelsInfo();
    }

    private void resultButtonsListener(boolean positiveAnswer) {
        Core.getInstance().learningService().changeEntryLevelAndStatistics(getCurrentEntry(), positiveAnswer);
        nextWord();
    }

    private Entry getCurrentEntry() {
        return entries.get(wordCount);
    }

    private void nextWord() {
        wordCount++;
        if (wordCount < entries.size()) {
            tuneLabelsForNewWord();
            tuneButtonsForNextWord();
        } else {
            finishLearning();
        }
    }

    private void tuneLabelsForNewWord() {
        wordLabel.setText(getCurrentEntry().getWord());
        countLabel.setText(String.format("%s/%s", wordCount + 1, entries.size()));
        String desc = String.format("Level: %s\nList: %s\nLast repeat: %s\nPage: %s", getCurrentEntry().getLevel(),
                getCurrentEntry().getWordList(),
                new SimpleDateFormat("dd.MM.yyyy").format(getCurrentEntry().getLastRepeat()), getCurrentEntry().getPage());
        levelLabel.setText(desc);
        translationLabel.setText("");
    }

    private void tuneButtonsForNextWord() {
        showTranslationButton.setDisable(false);
        okButton.setDisable(true);
        notOkButton.setDisable(true);
    }

    private void showTranslationButtonListener() {
        translationLabel.setText(getCurrentEntry().getTranslation());
        showTranslationButton.setDisable(true);
        okButton.setDisable(false);
        notOkButton.setDisable(false);
    }

    private void finishLearning() {
        VocUtils.getDialogBuilder().setDialogHeight(600).openInfoDialog("Learning is completed!\n\n"
                + Core.getInstance().learningService().getStatisticsReport());
        closeWindowThatContains(wordLabel);
    }

    private void setLevelsInfo() {
        Map<Integer, Integer> levelNumOfEntriesMap = new HashMap<>();
        entries.forEach(entry ->
                levelNumOfEntriesMap.put(entry.getLevel(), levelNumOfEntriesMap.getOrDefault(entry.getLevel(), 0) + 1));
        StringBuilder levelsInfo = new StringBuilder("[");
        levelNumOfEntriesMap.keySet().forEach(level ->
                levelsInfo.append(String.format("%d:%d ", level, levelNumOfEntriesMap.get(level))));
        levelsInfo.deleteCharAt(levelsInfo.length() - 1);
        levelsInfo.append("]");
        levelsInfoLabel.setText(levelsInfo.toString());
    }
}
