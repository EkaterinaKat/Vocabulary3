package com.katyshevtseva.vocabulary;

import com.katyshevtseva.vocabulary.view.controller.MainController;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder.DialogInfo.MAIN;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VocabularyWindowBuilder.getInstance().openDialog(MAIN, new MainController());
    }
}
