package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ro.mpp2025.interfaces.IGameRepo;

import java.util.ArrayList;
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
    public List<Game> findAllLost() {
        logger.info("Finding all lost games");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Game where won = false", Game.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Iterable<Game> findAll() {
        logger.info("Finding all the games");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Game ", Game.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Game> save(Game entity) {
        logger.info("Saving game: {}", entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("Saved game: {}", entity);
            return Optional.empty();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return Optional.ofNullable(entity);
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
