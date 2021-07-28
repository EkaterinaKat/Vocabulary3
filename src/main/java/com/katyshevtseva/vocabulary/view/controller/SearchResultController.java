package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

class SearchResultController implements FxController {
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

    @FXML
    private void initialize() {
        tuneTable();
    }

    void fillTable(String inputString) {
        List<Entry> entries = Core.getInstance().searchService().search(inputString);
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
    }
}
