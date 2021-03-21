package com.katyshevtseva.vocabulary.view.utils;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.CoreConstants;
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
                setIconImagePath(VocUtils.getIconImagePath()).setController(new MainController()).showWindow();
    }

    public void openLearningWindow(FxController controller) {
        new WindowBuilder("/fxml/learning.fxml").setTitle(CoreConstants.APP_NAME).setWidth(650).setHeight(500).
                setIconImagePath(VocUtils.getIconImagePath()).setController(controller).showWindow();
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
}
