package repository;

import domain.Employee;
import domain.validator.RepoException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class EmployeeHibernateRepository implements IEmployeeRepository {
    @Override
    public Optional<Employee> findByUsername(String username) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createSelectionQuery("from Employee where username = ?1",Employee.class).setParameter(1,username).getSingleResultOrNull());
        }
    }

    @Override
    public Optional<Employee> findOne(Integer integer) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createSelectionQuery("from Employee where id = ?1",Employee.class).setParameter(1,integer).getSingleResultOrNull());
        }
    }

    @Override
    public List<Employee> findAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Employee",Employee.class).list();
        }
    }

    @Override
    public Optional<Employee> save(Employee entity) throws RepoException {
        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return Optional.empty(); // Succes
        } catch (Exception e) {
            if (tx != null) tx.rollback(); // Anulare tranzacție la eroare
            throw new RepoException("Failed to save employee: " + e.getMessage());
        }
    }

    @Override
    public Optional<Employee> update(Employee entity) throws RepoException {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.merge(entity);
            return Optional.empty();
        }catch (Exception e) {
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public void delete(Integer integer) throws RepoException {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Optional<Employee> op = findOne(integer);
            op.ifPresent(session::remove);
        }
    }
}
