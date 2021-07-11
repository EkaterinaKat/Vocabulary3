package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.view.controller.dialog.EntryEditingDialogController;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;
import static com.katyshevtseva.fx.FxUtils.setImageOnButton;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;

class ListController implements FxController {
    private WordList currentWordList;
    private MainController mainController;
    private List<Entry> selectedEntries;
    @FXML
    private Button addWordButton;
    @FXML
    private Button editButton;
    @FXML
    private Button moveButton;
    @FXML
    private Button wordDeleteButton;
    @FXML
    private Button listArchiveButton;
    @FXML
    private Button listRenameButton;
    @FXML
    private TableView<Entry> table;
    @FXML
    private TableColumn<Entry, String> wordColumn;
    @FXML
    private TableColumn<Entry, String> translationColumn;
    @FXML
    private TableColumn<Entry, Integer> pageColumn;
    @FXML
    private TableColumn<Entry, Number> countColumn;
    @FXML
    private TableColumn<Entry, Integer> levelColumn;
    @FXML
    private TableColumn<Entry, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<Entry, String> dateColumn;
    @FXML
    private BorderPane root;
    @FXML
    private ComboBox<Order> orderComboBox;

    ListController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        tuneColumns();
        table.setEditable(true);
        setImageOnButton("images/plus.png", addWordButton, 15);
        setComboBoxItems(orderComboBox, Order.values(), Order.BY_DATE);
        orderComboBox.setOnAction(event -> updateTable());
        setVisibilityOfWordManagementButtons();
        adjustButtonListeners();
        setRowsColors();
    }

    private enum Order {
        BY_DATE, BY_LEVEL
    }

    void showWordListIfItIsNotNull(WordList wordList) {
        if (wordList == null)
            root.setVisible(false);
        else {
            this.currentWordList = wordList;
            root.setVisible(true);
            updateTable();
        }
    }

    private void tuneColumns() {
        countColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(
                table.getItems().indexOf(column.getValue()) + 1));
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        pageColumn.setCellValueFactory(new PropertyValueFactory<>("page"));
        dateColumn.setCellValueFactory(param -> {
            Entry entry = param.getValue();
            String value = Core.getInstance().entryLifecycleService().getRipenessInfo(entry);
            return new SimpleStringProperty(value);
        });
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
        wordDeleteButton.setVisible(selectedEntries != null && selectedEntries.size() > 0);
    }

    private void updateTable() {
        selectedEntries = new ArrayList<>();
        setVisibilityOfWordManagementButtons();
        ObservableList<Entry> entries = FXCollections.observableArrayList();
        entries.addAll(getSortedEntries());
        table.getItems().clear();
        table.setItems(entries);
    }

    private List<Entry> getSortedEntries() {
        if (orderComboBox.getValue() == Order.BY_DATE)
            return currentWordList.getEntriesSortedByDate();
        return currentWordList.getEntriesSortedByLevel();
    }

    private void adjustButtonListeners() {

        addWordButton.setOnAction(event -> {
            VocabularyWindowBuilder.getInstance().openEntryAddingDialog(new EntryEditingDialogController(
                    Core.getInstance().listService().getPageOfLastAddedWord(currentWordList),
                    (word, translation, page) -> {
                        Core.getInstance().listService().addEntryToList(word, translation, page, currentWordList);
                        updateTable();
                    }
            ));
        });

        listRenameButton.setOnAction(
                event -> new StandardDialogBuilder().openTextFieldDialog(currentWordList.getTitle(), (newTitle) -> {
                    Core.getInstance().catalogueService().renameList(currentWordList, newTitle);
                    mainController.updateListsTitlesInCatalogue();
                    mainController.catalogueListSelectionListener(currentWordList);
                }));

        listArchiveButton.setOnAction(
                event -> new StandardDialogBuilder().openQuestionDialog("Archive list?", (b) -> {
                    if (b) {
                        Core.getInstance().catalogueService().archiveList(currentWordList);
                        mainController.updateCatalogue();
                        showWordListIfItIsNotNull(null);
                    }
                }));

        editButton.setOnAction(event -> {
            Entry entryToEdit = selectedEntries.get(0);

            VocabularyWindowBuilder.getInstance().openEntryAddingDialog(new EntryEditingDialogController(
                    entryToEdit.getWord(), entryToEdit.getTranslation(), entryToEdit.getPage() == null ? 0 : entryToEdit.getPage(),
                    (word, translation, page) -> {
                        Core.getInstance().listService().editEntry(entryToEdit, word, translation, page);
                        updateTable();
                    }));
        });

        moveButton.setOnAction(event -> new StandardDialogBuilder().openComboBoxDialog(
                Core.getInstance().catalogueService().getCatalogue(), currentWordList,
                newWordList -> {
                    Core.getInstance().listService().moveEntries(selectedEntries, newWordList);
                    updateTable();
                }));

        wordDeleteButton.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Delete?", (b -> {
            if (b) {
                Core.getInstance().listService().deleteEntries(selectedEntries);
                updateTable();
            }
        })));
    }

    private void setRowsColors() {
        table.setRowFactory(new Callback<TableView<Entry>, TableRow<Entry>>() {
            @Override
            public TableRow<Entry> call(TableView<Entry> tableView) {
                return new TableRow<Entry>() {
                    @Override
                    protected void updateItem(Entry entry, boolean empty) {
                        super.updateItem(entry, empty);
                        if (entry != null) {
                            if (entry.getLevel() < CoreConstants.CRITICAL_LEVEL) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, "#FF9999"));
                            } else if (entry.getLevel() >= CoreConstants.MAX_LEVEL) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, "#AAFF99"));
                            } else {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, "#FFD299"));
                            }
                        }
                    }
                };
            }
        });
    }
}
