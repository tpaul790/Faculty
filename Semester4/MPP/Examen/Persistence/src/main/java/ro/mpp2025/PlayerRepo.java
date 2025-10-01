package ro.mpp2025;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ro.mpp2025.interfaces.IPlayerRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepo implements IPlayerRepo {
    private static final Logger logger = LogManager.getLogger(PlayerRepo.class);
    @Override
    public Optional<Player> findOne(Integer integer) {
        logger.info("Finding player with id '{}'", integer);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Player player = session
                    .createSelectionQuery("from Player where id = :id", Player.class)
                    .setParameter("id", integer)
                    .getSingleResultOrNull();

            if (player != null) {
                logger.info("Found player with id {}", player.getId());
            } else {
                logger.info("No player found with id '{}'", integer);
            }
            return Optional.ofNullable(player); // ofNullable e mai sigur
        } catch (Exception e) {
            logger.error("Error finding player by id '{}'", integer, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Player> findAll() {
        logger.info("Finding all the players");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            List<Player> players = session.createQuery("from Player", Player.class).list();
            tx.commit();
            return players;
        } catch (Exception e) {
            logger.error("Error finding all players", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Player> findBy(String nickname) {
        logger.info("Finding player with nickname '{}'", nickname);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Player player = session
                    .createSelectionQuery("from Player where nickname = :nickname", Player.class)
                    .setParameter("nickname", nickname)
                    .getSingleResultOrNull();

            if (player != null) {
                logger.info("Found player with id {}", player.getId());
            } else {
                logger.info("No player found with nickname '{}'", nickname);
            }
            return Optional.ofNullable(player); // ofNullable e mai sigur
        } catch (Exception e) {
            logger.error("Error finding player by nickname '{}'", nickname, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Player> save(Player entity) {
        logger.info("Saving player {}", entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("Player successfully saved with id {}", entity.getId());
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
    public Optional<Player> update(Player entity) {
        logger.info("Updating player {}", entity);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity); // face update
            tx.commit();
            logger.info("Player successfully updated with id {}", entity.getId());
            return Optional.of(entity); // returnăm entitatea actualizată
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                    logger.warn("Transaction rolled back due to error updating player {}", entity, e);
                } catch (Exception rollbackEx) {
                    logger.error("Error during rollback", rollbackEx);
                }
            }
            logger.error("Error updating player {}", entity, e);
            return Optional.empty();
        }
    }


    @Override
    public boolean delete(Integer id) {
        logger.info("Deleting player with id {}", id);
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Caut entitatea după id
            Player player = session.get(Player.class, id);
            if (player == null) {
                logger.warn("No player found with id {}", id);
                return false;
            }

            session.remove(player);  // Șterg entitatea
            tx.commit();
            logger.info("Player successfully deleted with id {}", id);
            return true; // Returnez entitatea ștearsă
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                    logger.warn("Transaction rolled back due to error deleting player with id {}", id, e);
                } catch (Exception rollbackEx) {
                    logger.error("Error during rollback", rollbackEx);
                }
            }
            logger.error("Error deleting player with id {}", id, e);
            return false;
        }
    }


    @Override
    public int size() {
        logger.info("Counting all players");
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Long count = session
                    .createQuery("select count(p) from Player p", Long.class)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            logger.error("Error counting players", e);
            return 0;
        }
    }
}
