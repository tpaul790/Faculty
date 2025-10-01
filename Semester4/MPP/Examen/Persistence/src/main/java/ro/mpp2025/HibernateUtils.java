package ro.mpp2025;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed())
            sessionFactory = createNewSessionFactory();
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        try {
            Properties properties = new Properties();

            // Încarcă fisierul hibernate.properties din resources
            try (InputStream input = HibernateUtils.class.getClassLoader().getResourceAsStream("hibernate.properties")) {
                if (input == null) {
                    throw new RuntimeException("Nu am gasit fisierul hibernate.properties in resources");
                }
                properties.load(input);
            }

            Configuration configuration = new Configuration();

            // Adauga entitatile (pe restul le inlantuiesc de asta)
            configuration
                    .addAnnotatedClass(Player.class)
                    .addAnnotatedClass(Animal.class)
                    .addAnnotatedClass(ro.mpp2025.Configuration.class)
                    .addAnnotatedClass(Game.class);

            // Seteaza proprietatile din fisier
            configuration.setProperties(properties);

            return configuration.buildSessionFactory();

        } catch (IOException e) {
            throw new RuntimeException("Eroare la incarcarea fisierului hibernate.properties", e);
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
