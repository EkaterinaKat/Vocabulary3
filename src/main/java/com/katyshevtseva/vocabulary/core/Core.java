package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.service.*;
import com.katyshevtseva.vocabulary.database.VocDaoImpl;

public class Core {
    private static Core INSTANCE = new Core();
    private VocDao vocDao = new VocDaoImpl();
    private CatalogueService catalogueService = new CatalogueService(vocDao);
    private SearchService searchService = new SearchService(vocDao);
    private ListService listService = new ListService(vocDao);
    private EntryLifecycleService entryLifecycleService = new EntryLifecycleService();
    private LearningService learningService = new LearningService(vocDao);

    public static Core getInstance() {
        return INSTANCE;
    }

    public CatalogueService catalogueService() {
        return catalogueService;
    }

    public SearchService searchService() {
        return searchService;
    }

    public ListService listService() {
        return listService;
    }

    public EntryLifecycleService entryLifecycleService() {
        return entryLifecycleService;
    }

    public LearningService learningService() {
        return learningService;
    }

}
