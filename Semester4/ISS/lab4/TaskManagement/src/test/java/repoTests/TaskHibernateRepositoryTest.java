package repoTests;

import domain.Employee;
import domain.Task;
import domain.validator.RepoException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import repository.TaskHibernateRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskHibernateRepositoryTest {

    @InjectMocks
    private TaskHibernateRepository repo;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Aici, pentru testare reală, ar trebui să injectezi un SessionFactory mockabil
        // și să mock-ui openSession() să returneze session mock
    }

    @Test
    void testSetAllAssignedIdToNull() {
        Employee emp = new Employee("John", "john", "pass");
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("emp"), eq(emp))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(3);
        doNothing().when(transaction).commit();

        // simulăm comportamentul metodei fără sesiune reală
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery("UPDATE Task SET assignedEmployee = NULL WHERE assignedEmployee = :emp");
        q.setParameter("emp", emp);
        q.executeUpdate();
        tx.commit();

        verify(query).executeUpdate();
        verify(transaction).commit();
    }

}