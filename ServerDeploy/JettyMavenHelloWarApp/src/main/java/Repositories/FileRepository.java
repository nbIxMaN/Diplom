package Repositories;

import Models.FileInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Closeable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FileRepository implements Repository<FileInfo>, Closeable{
    private SessionFactory sessionFactory;

    public FileRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<FileInfo> get(int id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.of(session.get(FileInfo.class, id));
        }
    }

//    public FileInfo get(int id) {
//        try(DBSession session = sessionFactory.openSession()){
//            String qlString = "SELECT f FROM FileInfo f WHERE f.id = ?1";
//            TypedQuery<FileInfo> query = session.createQuery(qlString, FileInfo.class);
//            query.setParameter(1, id);
//
//            return query.getSingleResult();
//        }
//    }

    @Override
    public List<FileInfo> get() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<FileInfo> fileCriteriaQuery = criteriaBuilder.createQuery(FileInfo.class);
            fileCriteriaQuery.select(fileCriteriaQuery.from(FileInfo.class));
            return session.createQuery(fileCriteriaQuery).getResultList();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public void add(FileInfo entity) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void add(FileInfo... entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Arrays.asList(entities).forEach(session::save);
            session.getTransaction().commit();
        }
    }

    @Override
    public void add(Collection<FileInfo> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            entities.forEach(session::save);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(FileInfo entity) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(FileInfo... entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Arrays.asList(entities).forEach(session::delete);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Collection<FileInfo> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            entities.forEach(session::delete);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(int id) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(get(id));
            session.getTransaction().commit();
        }
    }

//    @Override
//    public void remove(Predicate predicate) {
//        try(DBSession session = sessionFactory.openSession()){
//            session.beginTransaction();
//            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            CriteriaDelete<FileInfo> userCriteriaQuery = criteriaBuilder.createCriteriaDelete(FileInfo.class).where(predicate);
//            userCriteriaQuery.from(FileInfo.class);
//            List<FileInfo> aa = session.createQuery(userCriteriaQuery).getResultList();
//            session.getTransaction().commit();
//        }
//    }

    @Override
    public void close() {
        sessionFactory = null;
    }
}
