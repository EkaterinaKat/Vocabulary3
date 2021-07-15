package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.AddingStatistics;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.core.service.ListService;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

import static com.katyshevtseva.date.DateCorrector.getProperDate;
import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.disableNonNumericChars;

class EntryAddingDialogController implements FxController {
    private ListService service = Core.getInstance().listService();
    private NoArgsKnob tableUpdateKnob;
    private WordList wordList;
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
    @FXML
    private TableView<FrequentWord> tableView;
    @FXML
    private TableColumn<FrequentWord, String> wordColumn;
    @FXML
    private TableColumn<FrequentWord, String> translationColumn;
    @FXML
    private TableColumn<FrequentWord, String> statusColumn;
    @FXML
    private TableColumn<FrequentWord, Void> addButtonColumn;

    EntryAddingDialogController(NoArgsKnob tableUpdateKnob, WordList wordList) {
        this.tableUpdateKnob = tableUpdateKnob;
        this.wordList = wordList;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(okButton, wordTextField, translationTextField, pageTextField);
        pageTextField.setText("" + service.getPageOfLastAddedWord(wordList));
        VocUtils.addKeyboardLayoutCorrection(wordTextField, translationTextField);
        disableNonNumericChars(pageTextField);
        okButton.setOnAction(event -> okButtonListener());
        wordTextField.setOnAction(event -> translationTextField.requestFocus());
        translationTextField.setOnAction(event -> pageTextField.requestFocus());
        pageTextField.setOnAction(event -> {
            if (!okButton.isDisable())
                okButtonListener();
        });
        setCountLabelText();
        tuneTable();
        wordTextField.textProperty().addListener((observable, oldValue, newValue) -> fillTable(wordTextField.getText()));
    }

    private void okButtonListener() {
        service.addEntryToList(wordTextField.getText(), translationTextField.getText(),
                Integer.parseInt(pageTextField.getText()), wordList);
        tableUpdateKnob.execute();
        setCountLabelText();
        wordTextField.clear();
        translationTextField.clear();
        wordTextField.requestFocus();
    }

    private void addFrequentWordButtonListener(FrequentWord frequentWord) {
        service.addEntryToList(frequentWord, Integer.parseInt(pageTextField.getText()), wordList);
        tableUpdateKnob.execute();
        setCountLabelText();
        wordTextField.clear();
        translationTextField.clear();
        wordTextField.requestFocus();
    }

    private void setCountLabelText() {
        AddingStatistics addingStatistics = service.getTodayAddingStatisticsOrNull();
        if (addingStatistics == null)
            countLabel.setText(String.format("%s: %d", READABLE_DATE_FORMAT.format(getProperDate()), 0));
        else
            countLabel.setText(String.format("%s: %d", READABLE_DATE_FORMAT.format(addingStatistics.getDate()), addingStatistics.getNum()));
    }

    private void tuneTable() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        FxUtils.adjustButtonColumn(addButtonColumn, "Add", this::addFrequentWordButtonListener);
    }

    private void fillTable(String string) {
        List<FrequentWord> entries = Core.getInstance().searchService().searchFrequentWords(string);
        ObservableList<FrequentWord> observableList = FXCollections.observableArrayList();
        observableList.addAll(entries);
        tableView.setItems(observableList);
    }
}
