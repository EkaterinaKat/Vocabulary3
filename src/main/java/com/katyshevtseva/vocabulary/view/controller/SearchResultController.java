package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.Point;
import com.katyshevtseva.fx.TableUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.service.SearchService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

class SearchResultController implements FxController {
    private Point tableSize;
    @FXML
    private TableView<Entry> table;
    @FXML
    private TableColumn<Entry, String> wordColumn;
    @FXML
    private TableColumn<Entry, String> translationColumn;
    @FXML
    private TableColumn<Entry, Integer> pageColumn;
    @FXML
    private TableColumn<Entry, Integer> levelColumn;
    @FXML
    private TableColumn<Entry, String> listNameColumn;

    SearchResultController(Point tableSize) {
        this.tableSize = tableSize;
    }

    SearchResultController() {
    }

    @FXML
    private void initialize() {
        tuneTable();
        if (tableSize != null) {
            table.setMinWidth(tableSize.getX());
            table.setMaxWidth(tableSize.getX());
            table.setMinHeight(tableSize.getY());
            table.setMaxHeight(tableSize.getY());
        }
    }

    void fillTable(String inputString) {
        List<Entry> entries = SearchService.search(inputString);
        ObservableList<Entry> observableList = FXCollections.observableArrayList();
        observableList.addAll(entries);
        table.setItems(observableList);
    }

    private void tuneTable() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        pageColumn.setCellValueFactory(new PropertyValueFactory<>("page"));
        listNameColumn.setCellValueFactory(new PropertyValueFactory<>("wordList"));

        TableUtils.adjustRows(table, (entry, row) -> {
            if (entry != null) {
                row.setContextMenu(getContextMenu(entry));
            }
        });
    }

    private ContextMenu getContextMenu(Entry entry) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem idItem = new MenuItem("Copy Id");
        idItem.setOnAction(event -> GeneralUtils.saveToClipBoard(entry.getId().toString()));

        MenuItem entryItem = new MenuItem("Copy Entry");
        entryItem.setOnAction(event -> GeneralUtils.saveToClipBoard(entry.getWordAndTranslation()));

        contextMenu.getItems().addAll(idItem, entryItem);
        return contextMenu;
    }
}
