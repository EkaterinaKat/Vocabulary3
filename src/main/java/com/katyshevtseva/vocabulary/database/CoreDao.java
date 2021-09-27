package com.katyshevtseva.vocabulary.database;

import org.hibernate.Session;

import java.util.List;

public class CoreDao {

    <T> List<T> getAll(String className) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        String sql = "From " + className;
        List<T> lists = session.createQuery(sql).list();

        session.getTransaction().commit();

        return lists;
    }

    public <T> void saveNew(T t) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.save(t);

        session.getTransaction().commit();
    }

    public <T> void saveEdited(T t) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.update(t);

        session.getTransaction().commit();
    }

    public <T> void delete(T t) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.delete(t);

        session.getTransaction().commit();
    }
}
