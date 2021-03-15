package com.katyshevtseva.vocabulary.core;

import com.katyshevtseva.vocabulary.core.service.CatalogueService;
import com.katyshevtseva.vocabulary.core.service.EntryLifecycleService;
import com.katyshevtseva.vocabulary.core.service.ListService;
import com.katyshevtseva.vocabulary.core.service.SearchService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class Core implements InitializingBean {
    private static Core INSTANCE = new Core();
    private CatalogueService catalogueService = new CatalogueService();
    private SearchService searchService = new SearchService();
    private ListService listService = new ListService();
    private EntryLifecycleService entryLifecycleService = new EntryLifecycleService();

    public static Core getInstance() {
//        while (INSTANCE == null) {
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() {
        INSTANCE = this;
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

}
