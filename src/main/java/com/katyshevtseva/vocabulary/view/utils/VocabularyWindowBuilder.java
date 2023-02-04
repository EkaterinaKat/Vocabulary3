package com.katyshevtseva.vocabulary.view.utils;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.vocabulary.core.CoreConstants;
import javafx.scene.Node;

public class VocabularyWindowBuilder {
    private static final VocabularyWindowBuilder INSTANCE = new VocabularyWindowBuilder();

    private VocabularyWindowBuilder() {
    }

    public static VocabularyWindowBuilder getInstance() {
        return INSTANCE;
    }

    public Node getNode(NodeInfo nodeInfo, FxController controller) {
        return new WindowBuilder(nodeInfo.fileName).setController(controller).getNode();
    }

    public void openDialog(DialogInfo dialogInfo, FxController controller) {
        new WindowBuilder(dialogInfo.fileName)
                .setController(controller).setSize(dialogInfo.size)
                .setTitle(dialogInfo.title).showWindow();
    }

    public void openSortingWindow(FxController controller, NoArgsKnob onCloseListener) {
        new WindowBuilder("/fxml/sorting.fxml").setTitle(CoreConstants.APP_NAME).setSize(500, 650)
                .setOnWindowCloseEventHandler(event -> onCloseListener.execute()).setController(controller).showWindow();
    }

    public enum DialogInfo {
        LEARNING_FINISH("/fxml/learning_finish_dialog.fxml", new Size(600, 650), ""),
        ENTRY_ADDING("/fxml/entry_adding_dialog.fxml", new Size(850, 950), ""),
        ENTRY_EDITING("/fxml/entry_editing_dialog.fxml", new Size(250, 340), ""),
        LEARNING("/fxml/learning.fxml", new Size(500, 650), ""),
        FREQUENT_SECTION("/fxml/frequent_section.fxml", new Size(650, 950), ""),
        MAIN("/fxml/main.fxml", new Size(900, 1735), CoreConstants.APP_NAME);

        private final String fileName;
        private final Size size;
        private final String title;

        DialogInfo(String fileName, Size size, String title) {
            this.fileName = fileName;
            this.size = size;
            this.title = title;
        }
    }

    public enum NodeInfo {
        CATALOGUE("/fxml/catalogue.fxml"),
        LIST("/fxml/list.fxml"),
        SEARCH("/fxml/search.fxml"),
        SEARCH_RESULT("/fxml/search_result.fxml");

        private final String fileName;

        NodeInfo(String fileName) {
            this.fileName = fileName;
        }
    }
}
