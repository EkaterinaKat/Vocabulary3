package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.view.utils.VocUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

class CatalogueController implements FxController {
    private MainController mainController;
    private List<Label> labels;
    @FXML
    private Button newListButton;
    @FXML
    private Button learnButton;
    @FXML
    private VBox cataloguePane;

    CatalogueController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        newListButton.setOnAction(event -> new StandardDialogBuilder().setCssPath(VocUtils.getCssPath())
                .setIconPath(VocUtils.getLogoImagePath()).openTextFieldDialog("",
                        s -> {
                            Core.getInstance().catalogueService().createWordList(s);
                            updateCatalogue();
                        }));
        updateCatalogue();
    }

    private void updateCatalogue() {
        List<WordList> catalogue = Core.getInstance().catalogueService().getCatalogue();
        labels = new ArrayList<>();
        cataloguePane.getChildren().clear();
        for (WordList list : catalogue) {
            Label label = new Label(list.getTitle());
            label.setMinHeight(30);
            label.setOnMouseClicked(event -> {
                mainController.showList(list);
                for (Label label1 : labels) {
                    label1.setStyle(VocUtils.getNotSelectedListStyle());
                }
                label.setStyle(VocUtils.getSelectedListStyle());
            });
            labels.add(label);
            cataloguePane.getChildren().addAll(label, Utils.getPaneWithHeight(20));
        }
    }
}
