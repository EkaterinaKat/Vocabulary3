package com.katyshevtseva.vocabulary.core;

public class KeyboardLayoutManager {
    private static final char[] rus = {'й', 'ц', 'у', 'к', 'е', 'н', 'г', 'ш', 'щ', 'з', 'х', 'ъ', 'ф', 'ы', 'в', 'а',
            'п', 'р', 'о', 'л', 'д', 'ж', 'э', 'я', 'ч', 'с', 'м', 'и', 'т', 'ь', 'б', 'ю'};
    private static final char[] eng = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', 'a', 's', 'd', 'f',
            'g', 'h', 'j', 'k', 'l', ';', '\'', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.'};

    public static String changeToEng(String inString) {
        char[] chars = inString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char charInLowerCase = toLowerCase(chars[i]);
            for (int j = 0; j < rus.length; j++) {
                if (charInLowerCase == rus[j]) {
                    chars[i] = eng[j];
                }
            }
        }
        return new String(chars);
    }

    private static Character toLowerCase(Character character) {
        return character.toString().toLowerCase().charAt(0);
    }

    public static String changeToRus(String inString) {
        char[] chars = inString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char charInLowerCase = toLowerCase(chars[i]);
            for (int j = 0; j < eng.length; j++) {
                if (charInLowerCase == eng[j]) {
                    chars[i] = rus[j];
                }
            }
        }
        return new String(chars);
    }
}
