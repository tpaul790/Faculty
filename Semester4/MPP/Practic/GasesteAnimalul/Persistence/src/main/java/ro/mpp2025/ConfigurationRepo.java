package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ro.mpp2025.interfaces.IConfigurationRepo;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class ConfigurationRepo implements IConfigurationRepo {
    private static final Logger logger = LogManager.getLogger(ConfigurationRepo.class);
    @Override
    public Optional<Configuration> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Configuration> findAll() {
        logger.info("Finding all the configurations");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Configuration", Configuration.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Configuration> save(Configuration entity) {
        logger.info("Saving configuration: {}", entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<Configuration> update(Configuration entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
