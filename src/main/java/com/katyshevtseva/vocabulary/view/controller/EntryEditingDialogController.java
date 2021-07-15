package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.katyshevtseva.fx.FxUtils.*;

class EntryEditingDialogController implements FxController {
    private NoArgsKnob tableUpdateKnob;
    private Entry entry;
    @FXML
    private TextField wordTextField;
    @FXML
    private TextField translationTextField;
    @FXML
    private TextField pageTextField;
    @FXML
    private Button okButton;

    EntryEditingDialogController(NoArgsKnob tableUpdateKnob, Entry entry) {
        this.tableUpdateKnob = tableUpdateKnob;
        this.entry = entry;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(okButton, wordTextField, translationTextField, pageTextField);
        wordTextField.setText(entry.getWord());
        translationTextField.setText(entry.getTranslation());
        pageTextField.setText("" + (entry.getPage() == null ? 0 : entry.getPage()));
        VocUtils.addKeyboardLayoutCorrection(wordTextField, translationTextField);
        disableNonNumericChars(pageTextField);
        okButton.setOnAction(event -> okButtonListener());
        wordTextField.setOnAction(event -> translationTextField.requestFocus());
        translationTextField.setOnAction(event -> pageTextField.requestFocus());
        pageTextField.setOnAction(event -> {
            if (!okButton.isDisable())
                okButtonListener();
        });
        if (entry.getFrequentWord() != null) {
            wordTextField.setDisable(true);
            translationTextField.setDisable(true);
        }
    }

    private void okButtonListener() {
        Core.getInstance().listService().editEntry(entry, wordTextField.getText(), translationTextField.getText(),
                Integer.parseInt(pageTextField.getText()));
        tableUpdateKnob.execute();
        closeWindowThatContains(wordTextField);
    }
}
