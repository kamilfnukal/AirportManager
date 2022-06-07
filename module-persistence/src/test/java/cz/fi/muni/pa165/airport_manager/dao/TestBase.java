package cz.fi.muni.pa165.airport_manager.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * A generic abstract test class for all dao tests.
 * Allows to override beforeEach and afterEach methods in particular implementation of this class. These methods are called before and after each test.
 * Moreover, it provides helper methods to access database in transaction: {@link #findById(Long)}, {@link #findAll()}, {@link #persistEntities(List)}.
 *
 * @param <E> Entity of dao implementation
 * @author Matej Michálek
 */
public abstract class TestBase<E> extends AbstractTestNGSpringContextTests {

    private final Class<E> clazz;
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TransactionTemplate transaction;

    public TestBase(Class<E> clazz) {
        this.clazz = clazz;
    }

    @BeforeMethod
    protected void setup() {
        beforeEach();
    }

    @AfterMethod
    protected void cleanUp() {
        cleanDatabase();
        afterEach();
    }

    /**
     * This method can be overridden if custom implementation code has to be run before each test.
     */
    protected void beforeEach() {
    }

    /**
     * This method can be overridden if custom implementation code has to be run after each test.
     */
    protected void afterEach() {
    }

    protected void persistEntities(List<Object> entities) {
        transaction.executeWithoutResult((status) -> entities.forEach(entity -> em.persist(entity)));
    }

    protected E findById(Long id) {
        return em.find(clazz, id);
    }

    protected List<E> findAll() {
        var builder = em.getCriteriaBuilder();
        var criteriaQuery = builder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        return em.createQuery(criteriaQuery).getResultList();
    }

    private void cleanDatabase() {
        transaction.executeWithoutResult((status) -> {
            em.createQuery("delete from Manager").executeUpdate();
            em.createQuery("delete from Admin").executeUpdate();
            em.createQuery("delete from Steward").executeUpdate();
            em.createQuery("delete from Flight").executeUpdate();
            em.createQuery("delete from Airport").executeUpdate();
            em.createQuery("delete from Airplane").executeUpdate();
        });
    }
}
