package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.LearningStatistics;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.katyshevtseva.fx.Utils.closeWindowThatContains;

public class LearningController implements FxController {
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

    LearningController(List<Entry> entries) {
        this.entries = entries;
    }

    @FXML
    private void initialize() {
        Utils.setImageOnButton("images/tick.png", okButton, 25);
        Utils.setImageOnButton("images/red_cross.png", notOkButton, 25);
        okButton.setOnAction(event -> resultButtonsListener(true));
        notOkButton.setOnAction(event -> resultButtonsListener(false));
        showTranslationButton.setOnAction(event -> showTranslationButtonListener());
        wordCount = -1;
        nextWord();
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
        String desc = String.format("Level: %s\nList: %s\nLast repeat: %s", getCurrentEntry().getLevel(),
                getCurrentEntry().getWordList(),
                new SimpleDateFormat("dd.MM.yyyy").format(getCurrentEntry().getLastRepeat()));
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
        VocUtils.getDialogBuilder().openInfoDialog("Learning is completed!\n\n" + getStatisticsReport());
        closeWindowThatContains(wordLabel);
    }

    private String getStatisticsReport() {
        String report = "Statistics:\n";
        for (LearningStatistics statistics : Core.getInstance().learningService().getLearningStatistics()) {
            report += String.format("%s: %s/%s \n", statistics.getLevel(), statistics.getFalseNum(), statistics.getAllNum());
        }
        return report;
    }
}
