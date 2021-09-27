package com.katyshevtseva.vocabulary.database;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.*;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VocDaoImpl implements VocDao {
    private static final SimpleDateFormat postgresDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final CoreDao coreDao = new CoreDao();

    @Override
    public <T> void saveNew(T t) {
        coreDao.saveNew(t);
    }

    @Override
    public <T> void saveEdited(T t) {
        coreDao.saveEdited(t);
    }

    @Override
    public <T> void delete(T t) {
        coreDao.delete(t);
    }

    @Override
    public List<WordList> getAllWordLists() {
        return coreDao.getAll(WordList.class.getSimpleName());
    }

    @Override
    public List<Entry> getAllEntries() {
        return coreDao.getAll(Entry.class.getSimpleName());
    }

    @Override
    public List<LearningLog> getAllLearningLogs() {
        return coreDao.getAll(LearningLog.class.getSimpleName());
    }

    @Override
    public List<FrequentWord> getAllFrequentWords() {
        return coreDao.getAll(FrequentWord.class.getSimpleName());
    }

    @Override
    public LearningStatistics getLearningStatisticsOrNull(Date date, int level) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(LearningStatistics.class)
                .add(Restrictions.eq("level", level))
                .add(Restrictions.eq("date", date));
        List<LearningStatistics> statistics = criteria.list();

        session.getTransaction().commit();

        if (statistics.size() > 1)
            throw new RuntimeException();
        if (statistics.size() == 0)
            return null;

        return statistics.get(0);
    }

    @Override
    public List<LearningStatistics> getStatistics(Date date) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(LearningStatistics.class)
                .add(Restrictions.eq("date", date));
        List<LearningStatistics> statistics = criteria.list();

        session.getTransaction().commit();

        return statistics;
    }

    @Override
    public AddingStatistics getAddingStatisticsOrNull(Date date) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(AddingStatistics.class)
                .add(Restrictions.eq("date", date));
        List<AddingStatistics> statistics = criteria.list();

        session.getTransaction().commit();

        if (statistics.size() > 1)
            throw new RuntimeException();
        if (statistics.size() == 0)
            return null;

        return statistics.get(0);
    }

    @Override
    public int getPageOfLastAddedWord(WordList wordList) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Query query = session.createSQLQuery(
                        "select * from entry where word_list_id = :word_list_id order by creation_date desc limit 1; ")
                .addEntity(Entry.class)
                .setParameter("word_list_id", wordList.getId());

        List<Entry> entries = query.list();

        session.getTransaction().commit();

        if (entries.isEmpty() || entries.get(0).getPage() == null)
            return 0;

        return entries.get(0).getPage();
    }

    @Override
    public int countFrequentWordByStatus(Status status) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
                "select count(*) from FrequentWord fw where fw.status = :status ");
        query.setString("status", status.toString());

        return ((Long) query.uniqueResult()).intValue();
    }

    @Override
    public List<FrequentWord> getFrequentWordsByStatus(Status status) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
                "select fw from FrequentWord fw where fw.status = :status ");
        query.setString("status", status.toString());

        List<FrequentWord> words = query.list();
        session.getTransaction().commit();

        return words;
    }

    @Override
    public List<FrequentWord> searchFrequentWord(String term) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
                "select fw from FrequentWord fw where LOWER(fw.word) like concat('%', LOWER(:term), '%') ");
        query.setString("term", term);

        List<FrequentWord> words = query.list();
        session.getTransaction().commit();

        return words;
    }

    @Override
    public List<Entry> searchEntries(String term) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Query query = session.createQuery(
                "select e from Entry e " +
                        " where lower(e.word) like concat('%', lower(:term), '%') " +
                        " or lower(e.translation) like concat('%', lower(:term), '%') ");
        query.setString("term", term);

        List<Entry> entries = query.list();
        session.getTransaction().commit();

        return entries;
    }
}
