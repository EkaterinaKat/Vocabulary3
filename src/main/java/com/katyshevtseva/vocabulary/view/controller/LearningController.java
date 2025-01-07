package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.service.LearningService;
import com.katyshevtseva.vocabulary.view.utils.ImageUtils;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxImageCreationUtil.IconPicture.GREEN_TICK;
import static com.katyshevtseva.fx.FxImageCreationUtil.IconPicture.RED_CROSS;
import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;
import static com.katyshevtseva.fx.FxUtils.setImageOnButton;
import static com.katyshevtseva.vocabulary.core.CoreConstants.CRITICAL_LEVEL;
import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.DialogInfo.LEARNING_FINISH;

class LearningController implements FxController {
    private final List<Entry> entries;
    private final NoArgsKnob finishKnob;
    private final List<Entry> problematicWords = new ArrayList<>();
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
    @FXML
    private Button showContextButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Label contextLabel;

    LearningController(List<Entry> entries, NoArgsKnob finishKnob) {
        this.entries = entries;
        this.finishKnob = finishKnob;
    }

    @FXML
    private void initialize() {
        setImageOnButton(GREEN_TICK, okButton, 50);
        setImageOnButton(RED_CROSS, notOkButton, 50);
        okButton.setOnAction(event -> resultButtonsListener(true));
        notOkButton.setOnAction(event -> resultButtonsListener(false));
        showTranslationButton.setOnAction(event -> showTranslationButtonListener());
        showContextButton.setOnAction(event -> showContextButtonListener());
        wordCount = -1;
        nextWord();
        setLevelsInfo();
    }

    private void resultButtonsListener(boolean positiveAnswer) {
        if (!isLowLevelEntry() && !positiveAnswer) {
            problematicWords.add(getCurrentEntry());
        }
        LearningService.changeEntryLevelAndStatistics(getCurrentEntry(), positiveAnswer);
        nextWord();
    }

    private Entry getCurrentEntry() {
        return entries.get(wordCount);
    }

    private boolean isLowLevelEntry() {
        return getCurrentEntry().getLevel() < CRITICAL_LEVEL;
    }

    private void nextWord() {
        wordCount++;
        if (wordCount < entries.size()) {
            tuneControlsForNextWord();
        } else {
            finishLearning();
        }
    }

    private void tuneControlsForNextWord() {
        okButton.setDisable(true);
        notOkButton.setDisable(true);

        boolean contextButtonEnabled = ImageUtils.hasExampleOrImage(getCurrentEntry()) && isLowLevelEntry();
        showContextButton.setDisable(!contextButtonEnabled);
        showTranslationButton.setDisable(contextButtonEnabled);

        wordLabel.setText(getCurrentEntry().getWord());
        countLabel.setText(String.format("%s/%s", wordCount + 1, entries.size()));
        String desc = String.format("Level: %s\n", getCurrentEntry().getLevel());
        if (isLowLevelEntry()) {
            desc += String.format("%s (%s)\n", getCurrentEntry().getWordList(), getCurrentEntry().getPage());
        }
        desc += "\n";
        levelLabel.setText(desc);
        translationLabel.setText("");
        contextLabel.setText("");

        imageView.setImage(null);
    }

    private void showTranslationButtonListener() {
        translationLabel.setText(getCurrentEntry().getTranslation());
        if (!isLowLevelEntry()) {
            showContext();
        }
        showTranslationButton.setDisable(true);
        allowAnswer();
    }

    private void showContextButtonListener() {
        showTranslationButton.setDisable(false);
        showContextButton.setDisable(true);
        showContext();
    }

    private void showContext() {
        contextLabel.setText(getCurrentEntry().getExample());

        try {
            ImageContainer imageContainer = ImageUtils.getImageContainerOrNull(getCurrentEntry());
            if (imageContainer != null) {
                imageView.setImage(imageContainer.getImage());
            }
        } catch (Exception e) {
            new StandardDialogBuilder().setSize(300, 300).openInfoDialog(e.getMessage());
        }
    }

    private void allowAnswer() {
        okButton.setDisable(false);
        notOkButton.setDisable(false);
    }

    private void finishLearning() {
        VocabularyWindowBuilder.getInstance().openDialog(LEARNING_FINISH, new LearningFinishDialogController(problematicWords));
        finishKnob.execute();
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
