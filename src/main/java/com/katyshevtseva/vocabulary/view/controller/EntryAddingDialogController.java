package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.Point;
import com.katyshevtseva.fx.TableUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.core.service.ListService;
import com.katyshevtseva.vocabulary.core.service.SearchService;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.disableNonNumericChars;
import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.NodeInfo.SEARCH_RESULT;

class EntryAddingDialogController implements FxController {
    private final NoArgsKnob tableUpdateKnob;
    private final WordList wordList;
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
    @FXML
    private Pane searchResultPane;
    @FXML
    private TextArea exampleTextArea;
    @FXML
    private Button pageMinusButton;
    @FXML
    private Button pagePlusButton;

    EntryAddingDialogController(NoArgsKnob tableUpdateKnob, WordList wordList) {
        this.tableUpdateKnob = tableUpdateKnob;
        this.wordList = wordList;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(okButton, wordTextField, translationTextField, pageTextField);
        pageTextField.setText("" + ListService.getPageOfLastAddedWord(wordList));
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
        SearchResultController searchResultController = new SearchResultController(new Point(830, 250));
        searchResultPane.getChildren().add(VocabularyWindowBuilder.getInstance().getNode(SEARCH_RESULT, searchResultController));
        wordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            fillTable(wordTextField.getText().trim());
            searchResultController.fillTable(wordTextField.getText().trim());
        });
        translationTextField.textProperty().addListener(
                (observable, oldValue, newValue) -> searchResultController.fillTable(translationTextField.getText()));
        pagePlusButton.setOnAction(event -> pageChangeButtonListener(1));
        pageMinusButton.setOnAction(event -> pageChangeButtonListener(-1));
    }

    private void pageChangeButtonListener(int increase) {
        if (!GeneralUtils.isEmpty(pageTextField.getText())) {
            int page = Integer.parseInt(pageTextField.getText());
            pageTextField.setText((page + increase) + "");
        }
    }

    private void okButtonListener() {
        ListService.addEntryToList(wordTextField.getText(), translationTextField.getText(),
                Integer.parseInt(pageTextField.getText()), wordList, exampleTextArea.getText());
        tableUpdateKnob.execute();
        setCountLabelText();
        wordTextField.clear();
        translationTextField.clear();
        exampleTextArea.clear();
        wordTextField.requestFocus();
    }

    private void addFrequentWordButtonListener(FrequentWord frequentWord) {
        ListService.addEntryToList(frequentWord, Integer.parseInt(pageTextField.getText()), wordList, exampleTextArea.getText());
        tableUpdateKnob.execute();
        setCountLabelText();
        wordTextField.clear();
        translationTextField.clear();
        exampleTextArea.clear();
        wordTextField.requestFocus();
    }

    private void setCountLabelText() {
        countLabel.setText(String.format("%s: %d", READABLE_DATE_FORMAT.format(new Date()), ListService.getNumOfEntriesAddedToday()));
    }

    private void tuneTable() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableUtils.adjustButtonColumn(addButtonColumn, "Add", this::addFrequentWordButtonListener);

        TableUtils.adjustRows(tableView, (frequentWord, row) -> {
            if (frequentWord != null) {
                row.setContextMenu(getContextMenu(frequentWord));
            }
        });
    }

    private ContextMenu getContextMenu(FrequentWord frequentWord) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem idItem = new MenuItem("Copy Id");
        idItem.setOnAction(event -> GeneralUtils.saveToClipBoard(frequentWord.getId().toString()));

        MenuItem entryItem = new MenuItem("Copy Entry");
        entryItem.setOnAction(event -> GeneralUtils.saveToClipBoard(frequentWord.getWordAndTranslation()));

        contextMenu.getItems().addAll(idItem, entryItem);
        return contextMenu;
    }

    private void fillTable(String string) {
        List<FrequentWord> entries = SearchService.searchFrequentWords(string);
        ObservableList<FrequentWord> observableList = FXCollections.observableArrayList();
        observableList.addAll(entries);
        tableView.setItems(observableList);
    }
}
