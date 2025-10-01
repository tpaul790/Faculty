package ro.mpp2025;

import org.hibernate.Transaction;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ro.mpp2025.interfaces.IAttemptRepo;
import org.hibernate.Session;
import java.util.Optional;

public class AttemptRepo implements IAttemptRepo {
    private static final Logger logger = LogManager.getLogger(AttemptRepo.class);
    @Override
    public Optional<Attempt> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Attempt> findAll() {
        return null;
    }

    @Override
    public Optional<Attempt> save(Attempt entity) {
        logger.info("Saving attempt {}", entity);
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
    public Optional<Attempt> update(Attempt entity) {
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
