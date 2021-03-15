package com.katyshevtseva.vocabulary.view.utils;

public class VocUtils {

    public static String getCssPath() {
        return "/css/general_style.css";
    }

    public static String getLogoImagePath() {
        return "/images/logo.png";
    }

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
