package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.TableUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.core.service.CatalogueService;
import com.katyshevtseva.vocabulary.core.service.ListService;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.fx.FxImageCreationUtil.IconPicture.GREEN_PLUS;
import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;
import static com.katyshevtseva.fx.FxUtils.setImageOnButton;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;
import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.DialogInfo.ENTRY_ADDING;
import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.DialogInfo.ENTRY_EDITING;

class ListController implements FxController {
    private final MainController mainController;
    private WordList currentWordList;
    private List<Entry> selectedEntries;
    @FXML
    private Button addWordButton;
    @FXML
    private Button moveButton;
    @FXML
    private Button wordDeleteButton;
    @FXML
    private Button listArchiveButton;
    @FXML
    private Button listRenameButton;
    @FXML
    private Button freezeButton;
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
    private TableColumn<Entry, String> exampleColumn;
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
        setImageOnButton(GREEN_PLUS, addWordButton, 15);
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
        exampleColumn.setCellValueFactory(new PropertyValueFactory<>("example"));
        exampleColumn.setCellFactory(param -> new TableCell<Entry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setTooltip(new Tooltip(item));
            }
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
        moveButton.setVisible(selectedEntries != null && selectedEntries.size() > 0);
        wordDeleteButton.setVisible(selectedEntries != null && selectedEntries.size() > 0);
    }

    private void updateTable() {
        mainController.updateStatistics();

        selectedEntries = new ArrayList<>();
        setVisibilityOfWordManagementButtons();
        ObservableList<Entry> entries = FXCollections.observableArrayList();
        entries.addAll(getSortedEntries());
        table.getItems().clear();
        table.setItems(entries);
        adjustButtonListenersThatDependsOnNotNullCurrentWordList();
        listArchiveButton.setVisible(!currentWordList.isArchived());
    }

    // Настройка этих кнопок идет отдельно от настроек других кнопок так как
    // для настройки этой кнопки нужно знать currentWordList. Такой костыльчик
    private void adjustButtonListenersThatDependsOnNotNullCurrentWordList() {
        DcTextField titleField = new DcTextField(true, currentWordList.getTitle());
        listRenameButton.setOnAction(event -> DialogConstructor.constructDialog(() -> {
            CatalogueService.renameList(currentWordList, titleField.getValue());
            mainController.updateListsTitlesInCatalogue();
            mainController.catalogueListSelectionListener(currentWordList);
        }, titleField));

        DcComboBox<WordList> dcComboBox = new DcComboBox<>(true, currentWordList,
                CatalogueService.getCatalogue(CatalogueService.ListStatus.ACTIVE));
        moveButton.setOnAction(event -> DialogConstructor.constructDialog(() -> {
            ListService.moveEntries(selectedEntries, dcComboBox.getValue());
            updateTable();
        }, dcComboBox));
    }

    private List<Entry> getSortedEntries() {
        if (orderComboBox.getValue() == Order.BY_DATE)
            return currentWordList.getEntriesSortedByDate();
        return currentWordList.getEntriesSortedByLevel();
    }

    private void adjustButtonListeners() {

        addWordButton.setOnAction(event -> VocabularyWindowBuilder.getInstance().openDialog(ENTRY_ADDING,
                new EntryAddingDialogController(this::updateTable, currentWordList)));

        listArchiveButton.setOnAction(
                event -> new StandardDialogBuilder().openQuestionDialog("Archive list?", (b) -> {
                    if (b) {
                        CatalogueService.archiveList(currentWordList);
                        mainController.updateCatalogue();
                        mainController.updateStatistics();
                        showWordListIfItIsNotNull(null);
                    }
                }));

        wordDeleteButton.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Delete?", (b -> {
            if (b) {
                ListService.deleteEntries(selectedEntries);
                updateTable();
            }
        })));

        freezeButton.setOnAction(event -> {
            CatalogueService.freeze(currentWordList);
            mainController.updateCatalogue();
        });
    }

    private void setRowsColors() {
        TableUtils.adjustRows(table, (entry, row) -> {
            if (entry != null) {
                if (entry.getLevel() < CoreConstants.CRITICAL_LEVEL) {
                    row.setStyle(Styler.getColorfullStyle(BACKGROUND, "#FF9999"));
                } else if (entry.getLevel() >= CoreConstants.MAX_LEVEL) {
                    row.setStyle(Styler.getColorfullStyle(BACKGROUND, "#AAFF99"));
                } else {
                    row.setStyle(Styler.getColorfullStyle(BACKGROUND, "#FFD299"));
                }
                row.setContextMenu(getContextMenu(entry));
            }
        });
    }

    private ContextMenu getContextMenu(Entry entry) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> VocabularyWindowBuilder.getInstance().openDialog(ENTRY_EDITING,
                new EntryEditingDialogController(this::updateTable, entry)));

        MenuItem idItem = new MenuItem("Copy Id");
        idItem.setOnAction(event -> GeneralUtils.saveToClipBoard(entry.getId().toString()));

        MenuItem entryItem = new MenuItem("Copy Entry");
        entryItem.setOnAction(event -> GeneralUtils.saveToClipBoard(entry.getWordAndTranslation()));

        contextMenu.getItems().addAll(editItem, idItem, entryItem);
        return contextMenu;
    }
}
