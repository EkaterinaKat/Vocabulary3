package com.katyshevtseva.vocabulary.core;

import java.util.HashMap;
import java.util.Map;

public class CoreConstants {
    public static final String APP_NAME = "Vocabulary 3";
    public static final int MAX_LEVEL = 8;
    public static final int CRITICAL_LEVEL = 4;
    public static final Map<Integer, Integer> LEVEL_DAYS_MAP = new HashMap<>();

    static {
        LEVEL_DAYS_MAP.put(1, 3);
        LEVEL_DAYS_MAP.put(2, 4);
        LEVEL_DAYS_MAP.put(3, 6);
        LEVEL_DAYS_MAP.put(4, 8);
        LEVEL_DAYS_MAP.put(5, 10);
        LEVEL_DAYS_MAP.put(6, 12);
        LEVEL_DAYS_MAP.put(7, 15);
    }
}
