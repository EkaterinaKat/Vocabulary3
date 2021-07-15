package com.katyshevtseva.vocabulary.view.utils;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.view.controller.FrequentSectionController;
import com.katyshevtseva.vocabulary.view.controller.MainController;
import javafx.scene.Node;

public class VocabularyWindowBuilder {
    private static final VocabularyWindowBuilder INSTANCE = new VocabularyWindowBuilder();

    private VocabularyWindowBuilder() {
    }

    public static VocabularyWindowBuilder getInstance() {
        return INSTANCE;
    }

    public void openMainWindow() {
        new WindowBuilder("/fxml/main.fxml").setTitle(CoreConstants.APP_NAME).setWidth(1735).setHeight(900).
                setController(new MainController()).showWindow();
    }

    public void openFrequentSectionWindow() {
        new WindowBuilder("/fxml/frequent_section.fxml").setTitle(CoreConstants.APP_NAME).setWidth(950).setHeight(650).
                setController(new FrequentSectionController()).showWindow();
    }

    public void openSortingWindow(FxController controller, NoArgsKnob onCloseListener) {
        new WindowBuilder("/fxml/sorting.fxml").setTitle(CoreConstants.APP_NAME).setWidth(650).setHeight(500)
                .setOnWindowCloseEventHandler(event -> onCloseListener.execute()).setController(controller).showWindow();
    }

    public void openLearningWindow(FxController controller) {
        new WindowBuilder("/fxml/learning.fxml").setTitle(CoreConstants.APP_NAME).setWidth(650).setHeight(500).
                setController(controller).showWindow();
    }

    public Node getCatalogueNode(FxController controller) {
        return new WindowBuilder("/fxml/catalogue.fxml").setController(controller).getNode();
    }

    public Node getListNode(FxController controller) {
        return new WindowBuilder("/fxml/list.fxml").setController(controller).getNode();
    }

    public Node getSearchNode(FxController controller) {
        return new WindowBuilder("/fxml/search.fxml").setController(controller).getNode();
    }

    public void openEntryEditingDialog(FxController controller) {
        new WindowBuilder("/fxml/entry_editing_dialog.fxml").setWidth(340).setHeight(250).
                setController(controller).showWindow();
    }

    public void openEntryAddingDialog(FxController controller) {
        new WindowBuilder("/fxml/entry_adding_dialog.fxml").setWidth(800).setHeight(650).
                setController(controller).showWindow();
    }

    public void openLearningFinishDialog(FxController controller) {
        new WindowBuilder("/fxml/learning_finish_dialog.fxml").setWidth(650).setHeight(600).
                setController(controller).showWindow();
    }
}
