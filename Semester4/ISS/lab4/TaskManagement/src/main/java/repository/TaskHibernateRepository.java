package repository;

import domain.Employee;
import domain.Task;
import domain.validator.RepoException;
import jakarta.persistence.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TaskHibernateRepository implements ITaskRepository {
    @Override
    public void setAllAssignedIdToNull(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String sql = "UPDATE Task SET assignedEmployee = NULL WHERE assignedEmployee = :emp";
            session.createQuery(sql).setParameter("emp", employee).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Task> findOne(Integer integer) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createSelectionQuery("from Task where id = ?1", Task.class).setParameter(1,integer).getSingleResultOrNull());
        }
    }

    @Override
    public List<Task> findAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Task where solveTime is NULL and assignedEmployee is NULL",Task.class).list();
        }
    }

    @Override
    public Optional<Task> save(Task entity) throws RepoException {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return Optional.empty(); // Succes
        } catch (Exception e) {
            if (tx != null) tx.rollback(); // Anulare tranzacție la eroare
            throw new RepoException("Failed to save task: " + e.getMessage());
        }
    }

    @Override
    public Optional<Task> update(Task entity) throws RepoException {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "UPDATE Task SET assignedEmployee = :emp, solveTime = :time WHERE id = :id";
            int rowsAffected = session.createQuery(hql)
                    .setParameter("emp", entity.getAssignetEmployee())
                    .setParameter("time", entity.getSolveTime())
                    .setParameter("id", entity.getId())
                    .executeUpdate();
            transaction.commit();
            if(rowsAffected > 0) {
                return Optional.empty();
            }
            return Optional.of(entity);
        }catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Anulare tranzacție la eroare
            throw new RepoException("Failed to save task: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer integer) throws RepoException {

    }
}
