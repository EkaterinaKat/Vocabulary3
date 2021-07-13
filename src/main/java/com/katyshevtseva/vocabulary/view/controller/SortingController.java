package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.service.FrequentWordService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;
import static com.katyshevtseva.fx.FxUtils.setImageOnButton;

class SortingController implements FxController {
    private FrequentWordService service = Core.getInstance().frequentWordService();
    private NoArgsKnob chartUpdateKnob;
    private List<FrequentWord> words;
    private int wordCount = -1;
    @FXML
    private Label wordLabel;
    @FXML
    private Label translationLabel;
    @FXML
    private Label countLabel;
    @FXML
    private Button showTranslationButton;
    @FXML
    private Button okButton;
    @FXML
    private Button notOkButton;

    SortingController(NoArgsKnob chartUpdateKnob) {
        this.chartUpdateKnob = chartUpdateKnob;
    }

    @FXML
    private void initialize() {
        words = service.getAllIntactWords();
        setImageOnButton("images/tick.png", okButton, 25);
        setImageOnButton("images/red_cross.png", notOkButton, 25);
        okButton.setOnAction(event -> resultButtonsListener(true));
        notOkButton.setOnAction(event -> resultButtonsListener(false));
        showTranslationButton.setOnAction(event -> showTranslationButtonListener());
        nextWord();
    }

    private void resultButtonsListener(boolean positiveAnswer) {
        service.sort(getCurrentWord(), positiveAnswer);
        nextWord();
    }

    private FrequentWord getCurrentWord() {
        return words.get(wordCount);
    }

    private void nextWord() {
        wordCount++;
        if (wordCount < words.size()) {
            tuneLabelsForNewWord();
            tuneButtonsForNextWord();
        } else {
            finishLearning();
        }
    }

    private void tuneLabelsForNewWord() {
        wordLabel.setText(getCurrentWord().getWord());
        translationLabel.setText("");
        countLabel.setText(String.format("%s/%s", wordCount + 1, words.size()));
    }

    private void tuneButtonsForNextWord() {
        showTranslationButton.setDisable(false);
        okButton.setDisable(true);
        notOkButton.setDisable(true);
    }

    private void showTranslationButtonListener() {
        translationLabel.setText(getCurrentWord().getTranslation());
        showTranslationButton.setDisable(true);
        okButton.setDisable(false);
        notOkButton.setDisable(false);
    }

    private void finishLearning() {
        new StandardDialogBuilder().openInfoDialog("Слова закончились! Ура товарищи!");
        chartUpdateKnob.execute();
        closeWindowThatContains(wordLabel);
    }
}
