package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialog.controller.TwoTextFieldsDialogController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.KeyboardLayoutManager;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ListController implements FxController {
    private WordList wordList;
    private MainController mainController;
    private List<Entry> selectedEntries;
    @FXML
    private Button addWordButton;
    @FXML
    private Button editButton;
    @FXML
    private Button moveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Entry> table;
    @FXML
    private TableColumn<Entry, String> wordColumn;
    @FXML
    private TableColumn<Entry, String> translationColumn;
    @FXML
    private TableColumn<Entry, Number> countColumn;
    @FXML
    private TableColumn<Entry, Integer> levelColumn;
    @FXML
    private TableColumn<Entry, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<Entry, String> dateColumn;

    ListController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        tuneColumns();
        table.setEditable(true);
        Utils.setImageOnButton("images/plus.png", addWordButton, 15, 15);
        setVisibilityOfWordManagementButtons();
        addWordButton.setOnAction(event -> openWordsAddingWindow());
    }

    void showWordList(WordList wordList) {
        this.wordList = wordList;
        selectedEntries = new ArrayList<>();
        updateTable();
    }

    private void tuneColumns() {
        countColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(
                table.getItems().indexOf(column.getValue()) + 1));
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("lastRepeat"));
        checkBoxColumn.setCellValueFactory(param -> {
            Entry entry = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(false);
            booleanProperty.addListener((observable, oldValue, newValue) -> {
                if (newValue)
                    selectedEntries.add(entry);
                else
                    selectedEntries.remove(entry);
                setVisibilityOfWordManagementButtons();
            });
            return booleanProperty;
        });
        checkBoxColumn.setCellFactory(param -> {
            CheckBoxTableCell<Entry, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void setVisibilityOfWordManagementButtons() {
        editButton.setVisible(selectedEntries != null && selectedEntries.size() == 1);
        moveButton.setVisible(selectedEntries != null && selectedEntries.size() > 0);
        deleteButton.setVisible(selectedEntries != null && selectedEntries.size() > 0);
    }

    private void updateTable() {
        ObservableList<Entry> entries = FXCollections.observableArrayList();
        entries.addAll(wordList.getEntries());
        Collections.reverse(entries);
        table.setItems(entries);
    }

    private void openWordsAddingWindow() {
        TwoTextFieldsDialogController controller = new StandardDialogBuilder().setIconPath(VocUtils.getLogoImagePath())
                .setCssPath(VocUtils.getCssPath()).openTwoTextFieldsDialog("", "", false,
                        (text1, text2) -> {
                            Core.getInstance().listService().addEntryToList(text1, text2, wordList);
                            updateTable();
                        });

        TextField wordTextField = controller.getTextField1();
        TextField translationField = controller.getTextField2();

        wordTextField.textProperty().addListener((observable, oldValue, newValue) ->
                wordTextField.setText(KeyboardLayoutManager.changeToEng(newValue)));
        translationField.textProperty().addListener((observable, oldValue, newValue) ->
                translationField.setText(KeyboardLayoutManager.changeToRus(newValue)));
    }
}
