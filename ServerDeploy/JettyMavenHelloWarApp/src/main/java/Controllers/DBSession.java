package Controllers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by alex on 22.04.17.
 */
public class DBSession {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    public static SessionFactory get(){
        return sessionFactory;
    }
}
