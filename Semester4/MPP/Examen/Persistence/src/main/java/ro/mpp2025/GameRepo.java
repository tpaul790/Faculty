package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ro.mpp2025.interfaces.IGameRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class GameRepo implements IGameRepo {
    private static final Logger logger = LogManager.getLogger(GameRepo.class);
    @Override
    public Optional<Game> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Game> findAll() {
        logger.info("Finding all the games");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            List<Game> games = session.createQuery("from Game", Game.class).list();
            tx.commit();
            return games;
        } catch (Exception e) {
            logger.error("Error finding all games", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Game> save(Game entity) {
        logger.info("Saving game {}", entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("Game successfully saved with id {}", entity.getId());
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
            logger.error("Error saving player {}", entity, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Game> update(Game entity) {
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
