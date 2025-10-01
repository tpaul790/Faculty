package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ro.mpp2025.interfaces.IConfigurationRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ConfigurationRepo implements IConfigurationRepo {
    private static final Logger logger = LogManager.getLogger(PlayerRepo.class);

    @Override
    public Optional<Configuration> findOne(Integer integer) {
        logger.info("Finding cong with id '{}'", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Configuration cong = session
                    .createSelectionQuery("from Configuration where id = :id", Configuration.class)
                    .setParameter("id", integer)
                    .getSingleResultOrNull();

            if (cong != null) {
                logger.info("Found cong with id {}", cong.getId());
            } else {
                logger.info("No cong found with id '{}'", integer);
            }
            return Optional.ofNullable(cong); // ofNullable e mai sigur
        } catch (Exception e) {
            logger.error("Error finding cong by id '{}'", integer, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Configuration> findAll() {
        logger.info("Finding all the players");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            List<Configuration> confs = session.createQuery("from Configuration ", Configuration.class).list();
            tx.commit();
            return confs;
        } catch (Exception e) {
            logger.error("Error finding all players", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Configuration> save(Configuration entity) {
        logger.info("Saving configuration {}", entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("Configuration successfully saved with id {}", entity.getId());
            return Optional.of(entity); // Returnăm entitatea salvată
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                    logger.warn("Transaction rolled back due to error", e);
                } catch (Exception rollbackEx) {
                    logger.error("Error during rollback", rollbackEx);
                }
            }
            logger.error("Error saving configuration {}", entity, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Configuration> update(Configuration entity) {
        logger.info("Updating configuration {}", entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity); // face update
            tx.commit();
            logger.info("Configuration successfully updated with id {}", entity.getId());
            return Optional.of(entity); // returnăm entitatea actualizată
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                    logger.warn("Transaction rolled back due to error updating configuration {}", entity, e);
                } catch (Exception rollbackEx) {
                    logger.error("Error during rollback", rollbackEx);
                }
            }
            logger.error("Error updating configuration {}", entity, e);
            return Optional.empty();
        }
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
