package com.katyshevtseva.vocabulary.view.utils;

import com.katyshevtseva.vocabulary.core.KeyboardLayoutManager;
import javafx.scene.control.TextField;

public class VocUtils {

    public static String getSelectedListStyle() {
        return "    -fx-text-fill: #66CC00;\n" +
                "    -fx-font-size: 20 px;\n" +
                "    -fx-font-weight: bold; ";
    }

    public static String getNotSelectedListStyle() {
        return "       -fx-text-fill: #006400;\n" +
                "       -fx-font-weight: bold;";
    }

    public static void addKeyboardLayoutCorrection(TextField wordTextField, TextField translationTextField) {
        wordTextField.textProperty().addListener((observable, oldValue, newValue) ->
                wordTextField.setText(KeyboardLayoutManager.changeToEng(newValue)));
        translationTextField.textProperty().addListener((observable, oldValue, newValue) ->
                translationTextField.setText(KeyboardLayoutManager.changeToRus(newValue)));
    }
}
