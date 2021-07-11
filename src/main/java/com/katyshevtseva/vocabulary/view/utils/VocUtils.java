package com.katyshevtseva.vocabulary.view.utils;

public class VocUtils {

    public static String getSelectedListStyle() {
        return "    -fx-text-fill: #66CC00;\n" +
                "    -fx-font-size: 20 px;\n" +
                "    -fx-font-weight: bold; ";
    }

    public static String getNotSelectedListStyle() {
        return "       -fx-text-fill: #006400;\n" +
                "       -fx-font-weight: bold;";
    }
}
