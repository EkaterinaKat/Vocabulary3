package com.katyshevtseva.vocabulary.database;

import com.katyshevtseva.vocabulary.core.VocDao;
import com.katyshevtseva.vocabulary.core.entity.AddingStatistics;
import com.katyshevtseva.vocabulary.core.entity.Entry;
import com.katyshevtseva.vocabulary.core.entity.LearningStatistics;
import com.katyshevtseva.vocabulary.core.entity.WordList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VocDaoImpl implements VocDao {
    private static final SimpleDateFormat postgresDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<WordList> getAllWordLists() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        String sql = "From " + WordList.class.getSimpleName();
        List<WordList> lists = session.createQuery(sql).list();

        session.getTransaction().commit();

        return lists;
    }

    @Override
    public void saveNewWordList(WordList wordList) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.save(wordList);

        session.getTransaction().commit();
    }

    @Override
    public void saveEditedWordList(WordList wordList) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.update(wordList);

        session.getTransaction().commit();
    }

    @Override
    public void saveNewEntry(Entry entry) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.save(entry);

        session.getTransaction().commit();
    }

    @Override
    public void saveEditedEntry(Entry entry) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.update(entry);

        session.getTransaction().commit();
    }

    @Override
    public List<Entry> getAllEntries() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        String sql = "From " + Entry.class.getSimpleName();
        List<Entry> entries = session.createQuery(sql).list();

        session.getTransaction().commit();

        return entries;
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
    public void saveNewLearningStatistics(LearningStatistics learningStatistics) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.save(learningStatistics);

        session.getTransaction().commit();
    }

    @Override
    public void saveEditedLearningStatistics(LearningStatistics learningStatistics) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.update(learningStatistics);

        session.getTransaction().commit();
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
    public void deleteEntry(Entry entry) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.delete(entry);

        session.getTransaction().commit();
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
    public void saveNewAddingStatistics(AddingStatistics addingStatistics) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.save(addingStatistics);

        session.getTransaction().commit();
    }

    @Override
    public void saveEditedAddingStatistics(AddingStatistics addingStatistics) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        session.update(addingStatistics);

        session.getTransaction().commit();
    }
}
