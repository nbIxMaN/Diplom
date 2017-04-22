package Repositories;

import Models.FileInfo;
import Models.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Closeable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User>, Closeable{
    private SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> get(int id) {
        try(Session session = sessionFactory.openSession()){
            return Optional.of(session.get(User.class, id));
        }
    }

    public User get(String login, String password) {
        try(Session session = sessionFactory.openSession()){
            String qlString = "SELECT u FROM User u WHERE u.login = ?1 AND u.password = ?2";
            TypedQuery<User> query = session.createQuery(qlString, User.class);
            query.setParameter(1, login);
            query.setParameter(2, password);
            User user = query.getSingleResult();
            Hibernate.initialize(user.getSetOfFile());
            return user;
        }
        catch (NoResultException e){
            return null;
        }
    }

    public User get(String cookie) {
        try(Session session = sessionFactory.openSession()){
            String qlString = "SELECT u FROM User u WHERE u.cookie = ?1";
            TypedQuery<User> query = session.createQuery(qlString, User.class);
            query.setParameter(1, cookie);
            User user = query.getSingleResult();
            Hibernate.initialize(user.getSetOfFile());
            return user;
        }
    }

    @Override
    public List<User> get() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
            userCriteriaQuery.select(userCriteriaQuery.from(User.class));
            return session.createQuery(userCriteriaQuery).getResultList();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public void add(User entity) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void add(User... entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Arrays.asList(entities).forEach(session::save);
            session.getTransaction().commit();
        }
    }

    @Override
    public void add(Collection<User> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            entities.forEach(session::save);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(User entity) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(User... entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Arrays.asList(entities).forEach(session::delete);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(Collection<User> entities) {
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

    public void update(User user, String cookie) {
        try(Session session = sessionFactory.openSession()){
            User u = get(user.getLogin(), user.getPassword());
            u.setCookie(cookie);
            session.beginTransaction();
            session.update(u);
            session.getTransaction().commit();
        }
    }

    public void update(User user, FileInfo fileInfo) {
        try(Session session = sessionFactory.openSession()){
            User u = get(user.getLogin(), user.getPassword());
            u.getSetOfFile().add(fileInfo);
            session.beginTransaction();
            session.update(u);
            session.getTransaction().commit();
        }
    }

//    @Override
//    public void remove(Predicate predicate) {
//        try(DBSession session = sessionFactory.openSession()){
//            session.beginTransaction();
//            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//            CriteriaDelete<User> userCriteriaQuery = criteriaBuilder.createCriteriaDelete(User.class).where(predicate);
//            userCriteriaQuery.from(User.class);
//            List<User> aa = session.createQuery(userCriteriaQuery).getResultList();
//            session.getTransaction().commit();
//        }
//    }

    @Override
    public void close() {
        sessionFactory = null;
    }
}
