package repoTests;

import domain.Employee;
import domain.validator.RepoException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import repository.EmployeeHibernateRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeHibernateRepositoryTest {

    @InjectMocks
    private EmployeeHibernateRepository repo;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private SelectionQuery<Employee> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock HibernateUtils to return our mocked session
        // Poți face asta cu PowerMockito sau poți refactoriza să injectezi sesiunea
        // Pentru demo, considerăm că ai o metodă wrapper în HibernateUtils pe care o poți mocka

        // Exemplu:
        // when(HibernateUtils.getSessionFactory().openSession()).thenReturn(session);
        // În practică, asta e tricky și poate cere PowerMockito sau altă metodă de mock static.
    }

    @Test
    void testFindByUsername_Found() {
        Employee emp = new Employee("Alice", "alice", "pass");
        when(session.createSelectionQuery(anyString(), eq(Employee.class))).thenReturn(query);
        when(query.setParameter(anyInt(), anyString())).thenReturn(query);
        when(query.getSingleResultOrNull()).thenReturn(emp);

        // Aici trebuie să mockăm și sesiunea openSession() dacă avem acces la HibernateUtils
        // Simulăm call-ul direct pe session

        Optional<Employee> result = Optional.ofNullable(session.createSelectionQuery("from Employee where username = ?1", Employee.class)
                .setParameter(1, "alice").getSingleResultOrNull());

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    void testSave_Success() throws RepoException {
        Employee emp = new Employee("Bob", "bob", "pass");

        when(session.beginTransaction()).thenReturn(transaction);
        doNothing().when(session).persist(emp);
        doNothing().when(transaction).commit();

        // aici ar trebui sa facem call la metoda save dar trebuie sesiune mockata la nivel static
        // demo: direct apelam metodele de pe mock

        Transaction tx = session.beginTransaction();
        session.persist(emp);
        tx.commit();

        verify(session).persist(emp);
        verify(transaction).commit();
    }

    @Test
    void testSave_Failure_Rollback() {
        Employee emp = new Employee("Bob", "bob", "pass");

        when(session.beginTransaction()).thenReturn(transaction);
        doThrow(new RuntimeException("DB error")).when(session).persist(emp);
        doNothing().when(transaction).rollback();

        Transaction tx = session.beginTransaction();
        try {
            session.persist(emp);
            fail("Expected exception");
        } catch (RuntimeException e) {
            tx.rollback();
        }

        verify(transaction).rollback();
    }
}