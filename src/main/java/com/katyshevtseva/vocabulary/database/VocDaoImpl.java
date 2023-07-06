package com.katyshevtseva.vocabulary.database;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.hibernate.CoreDao;
import com.katyshevtseva.hibernate.HibernateUtil;
import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;
import com.katyshevtseva.vocabulary.core.entity.LearningLog;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

public class VocDaoImpl implements VocDao {
    private final CoreDao coreDao = new CoreDao();

    @Override
    public <T> void saveNew(T t) {
        coreDao.saveNew(t);
    }

    @Override
    public <T> void saveEdited(T t) {
        coreDao.update(t);
    }

    @Override
    public <T> void delete(T t) {
        coreDao.delete(t);
    }

    @Override
    public List<WordList> findListsByArchived(boolean archived) {
        return coreDao.find(WordList.class, Restrictions.eq("archived", archived));
    }

    @Override
    public List<Entry> getAllEntries() {
        return coreDao.getAll(Entry.class.getSimpleName());
    }

    @Override
    public List<Entry> getEntriesByCreationDate(Date date) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        date = DateUtils.removeTimeFromDate(date);
        Date nextDay = DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, 1);
        Criteria criteria = session.createCriteria(Entry.class)
                .add(Restrictions.between("creationDate", date, nextDay));
        List<Entry> entries = criteria.list();

        session.getTransaction().commit();

        return entries;
    }

    @Override
    public List<LearningLog> getLearningLogsByEntry(Entry entry) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(LearningLog.class)
                .add(Restrictions.eq("entry", entry));
        List<LearningLog> logs = criteria.list();

        session.getTransaction().commit();

        return logs;
    }

    @Override
    public List<LearningLog> getLearningLogsByDate(Date date) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Criteria criteria = session.createCriteria(LearningLog.class).add(Restrictions.eq("date", date));
        List<LearningLog> statistics = criteria.list();

        session.getTransaction().commit();

        return statistics;
    }

    @Override
    public int getNumOfAddedEntriesByDate(Date date) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        Date fromDate = DateUtils.removeTimeFromDate(new Date());
        Date toDate = DateUtils.removeTimeFromDate(DateUtils.shiftDate(new Date(), DateUtils.TimeUnit.DAY, 1));

        Criteria criteria = session.createCriteria(Entry.class)
                .add(Restrictions.ge("creationDate", fromDate))
                .add(Restrictions.le("creationDate", toDate));
        List<Entry> entries = criteria.list();

        session.getTransaction().commit();

        return entries.size();
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
