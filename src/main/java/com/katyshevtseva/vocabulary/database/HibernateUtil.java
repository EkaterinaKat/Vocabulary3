package com.katyshevtseva.vocabulary.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.File;

class HibernateUtil {
    private static final Session session = buildSession();

    private static Session buildSession() {
        try {
            File file = new File("D://Code//Vocabulary3//src//main//resources//hibernate.cfg.xml");
            SessionFactory factory = new AnnotationConfiguration().configure(file).buildSessionFactory();
            return factory.openSession();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    static Session getSession() {
        return session;
    }
}

