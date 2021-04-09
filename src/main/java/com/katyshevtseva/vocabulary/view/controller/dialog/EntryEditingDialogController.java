package com.katyshevtseva.vocabulary.view.controller.dialog;

import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.KeyboardLayoutManager;
import com.katyshevtseva.vocabulary.core.entity.AddingStatistics;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;

import static com.katyshevtseva.date.DateCorrector.getProperDate;
import static com.katyshevtseva.fx.Utils.closeWindowThatContains;
import static com.katyshevtseva.fx.Utils.disableNonNumericChars;
import static com.katyshevtseva.vocabulary.view.controller.dialog.EntryEditingDialogController.DialogPurpose.CREATION;
import static com.katyshevtseva.vocabulary.view.controller.dialog.EntryEditingDialogController.DialogPurpose.EDITING;

public class EntryEditingDialogController implements FxController {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy");
    @FXML
    private Label countLabel;
    @FXML
    private TextField wordTextField;
    @FXML
    private TextField translationTextField;
    @FXML
    private TextField pageTextField;
    @FXML
    private Button okButton;
    private OkButtonHandler okButtonHandler;
    private String initWord;
    private String initTranslation;
    private int initPage;
    private DialogPurpose dialogPurpose;

    public EntryEditingDialogController(
            String initWord,
            String initTranslation,
            int initPage,
            OkButtonHandler okButtonHandler) {
        dialogPurpose = EDITING;
        this.okButtonHandler = okButtonHandler;
        this.initWord = initWord;
        this.initTranslation = initTranslation;
        this.initPage = initPage;
    }

    public EntryEditingDialogController(
            OkButtonHandler okButtonHandler) {
        dialogPurpose = CREATION;
        this.okButtonHandler = okButtonHandler;
    }

    enum DialogPurpose {
        CREATION, EDITING
    }

    @FXML
    private void initialize() {
        Utils.associateButtonWithControls(okButton, wordTextField, translationTextField, pageTextField);
        if (dialogPurpose == EDITING) {
            wordTextField.setText(initWord);
            translationTextField.setText(initTranslation);
            pageTextField.setText("" + initPage);
        }
        addKeyboardLayoutCorrection();
        disableNonNumericChars(pageTextField);
        okButton.setOnAction(event -> okButtonListener());
        wordTextField.setOnAction(event -> translationTextField.requestFocus());
        translationTextField.setOnAction(event -> pageTextField.requestFocus());
        pageTextField.setOnAction(event -> {
            if (!okButton.isDisable())
                okButtonListener();
        });
        setCountLabelText();
    }

    private void setCountLabelText() {
        if (dialogPurpose == CREATION) {
            AddingStatistics addingStatistics = Core.getInstance().listService().getTodayAddingStatisticsOrNull();
            if (addingStatistics == null)
                countLabel.setText(String.format("%s: %d", dateFormat.format(getProperDate()), 0));
            else
                countLabel.setText(String.format("%s: %d", dateFormat.format(addingStatistics.getDate()), addingStatistics.getNum()));
        }
    }

    private void okButtonListener() {
        okButtonHandler.execute(wordTextField.getText(), translationTextField.getText(), Integer.parseInt(pageTextField.getText()));
        setCountLabelText();

        if (dialogPurpose == EDITING)
            closeWindowThatContains(wordTextField);
        else {
            wordTextField.clear();
            translationTextField.clear();
            wordTextField.requestFocus();
        }
    }

    private void addKeyboardLayoutCorrection() {
        wordTextField.textProperty().addListener((observable, oldValue, newValue) ->
                wordTextField.setText(KeyboardLayoutManager.changeToEng(newValue)));
        translationTextField.textProperty().addListener((observable, oldValue, newValue) ->
                translationTextField.setText(KeyboardLayoutManager.changeToRus(newValue)));
    }

    @FunctionalInterface
    public interface OkButtonHandler {
        void execute(String word, String translation, int page);
    }
}
