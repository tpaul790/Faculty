package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ro.mpp2025.interfaces.IPlayerRepo;

import java.util.Optional;

public class PlayerRepo implements IPlayerRepo {
    private static final Logger logger = LogManager.getLogger(PlayerRepo.class);
    @Override
    public Optional<Player> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Player> findAll() {
        return null;
    }

    @Override
    public Optional<Player> findBy(String nickname) {
        logger.info("Finding player with nickname {}", nickname);
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Player player = session.createSelectionQuery("from Player where nickname = ?1",Player.class).setParameter(1,nickname).getSingleResultOrNull();
            logger.info("Found player with id {}", player.getId());
            return Optional.of(player);
        }
    }

    @Override
    public Optional<Player> save(Player entity) {
        logger.info("Saving player " + entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return Optional.empty();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<Player> update(Player entity) {
        logger.info("Update player " + entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return Optional.empty();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return Optional.ofNullable(entity);
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
