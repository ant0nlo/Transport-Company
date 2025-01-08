package main.configuration;

import main.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;


    static {
        try {
            // Създаване на sessionFactory
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Company.class)
                    .addAnnotatedClass(Vehicle.class)
                    .addAnnotatedClass(Employee.class)
                    .addAnnotatedClass(Transport.class)
                    .addAnnotatedClass(Client.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
    public static void initializeSchema() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createNativeQuery("""
            CREATE TABLE IF NOT EXISTS company (
                companyId BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(20) NOT NULL,
                address VARCHAR(255),
                revenue DECIMAL(17,2),
                is_deleted BOOLEAN NOT NULL
            )
        """).executeUpdate();

            session.getTransaction().commit();
        }
    }
}