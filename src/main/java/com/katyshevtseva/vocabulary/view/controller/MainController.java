package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.FxImageCreationUtil;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import com.katyshevtseva.vocabulary.core.service.CatalogueService;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;

import static com.katyshevtseva.fx.FxImageCreationUtil.IconPicture.VOC_LOGO;
import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.NodeInfo.*;

public class MainController implements FxController {
    private final ListController listController = new ListController(this);
    private final CatalogueController catalogueController = new CatalogueController(this);
    @FXML
    private Pane cataloguePane;
    @FXML
    private Pane searchPane;
    @FXML
    private Pane listPane;
    @FXML
    private ImageView logoImageView;
    @FXML
    private GridPane statisticsPane;

    @FXML
    private void initialize() {
        logoImageView.setImage(FxImageCreationUtil.getIcon(VOC_LOGO));
        cataloguePane.getChildren().add(VocabularyWindowBuilder.getInstance().getNode(CATALOGUE, catalogueController));
        searchPane.getChildren().add(VocabularyWindowBuilder.getInstance().getNode(SEARCH, new SearchController()));
        listPane.getChildren().add(VocabularyWindowBuilder.getInstance().getNode(LIST, listController));


        updateStatistics();
    }

    public void updateStatistics() {
        statisticsPane.getChildren().clear();
        int columnNum = 2;
        List<String> statistics = CatalogueService.getStatistics();
        for (int i = 0; i < statistics.size(); i++) {
            statisticsPane.add(new Label(statistics.get(i)),
                    GeneralUtils.getColumnByIndexAndColumnNum(i, columnNum),
                    GeneralUtils.getRowByIndexAndColumnNum(i, columnNum));
        }
    }

    void showList(WordList wordList) {
        listController.showWordListIfItIsNotNull(wordList);
    }

    void updateListsTitlesInCatalogue() {
        catalogueController.updateListsTitles();
    }

    void updateCatalogue() {
        catalogueController.updateCatalogue();
    }

    void catalogueListSelectionListener(WordList wordList) {
        catalogueController.listSelectionListener(wordList);
    }

}
