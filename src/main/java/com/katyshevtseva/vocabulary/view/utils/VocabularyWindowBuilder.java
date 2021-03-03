package com.katyshevtseva.vocabulary.view.utils;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.vocabulary.core.CoreConstants;
import com.katyshevtseva.vocabulary.view.MainController;

public class VocabularyWindowBuilder {
    private static final VocabularyWindowBuilder INSTANCE = new VocabularyWindowBuilder();

    private VocabularyWindowBuilder() {
    }

    public static VocabularyWindowBuilder getInstance() {
        return INSTANCE;
    }

    public void openMainWindow() {
        new WindowBuilder("/fxml/main.fxml").setTitle(CoreConstants.APP_NAME).setController(new MainController()).showWindow();
    }
}
