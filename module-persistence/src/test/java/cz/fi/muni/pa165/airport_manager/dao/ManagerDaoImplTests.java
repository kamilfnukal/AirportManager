package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.PersistenceAirportManagerContext;

import cz.fi.muni.pa165.airport_manager.entity.Manager;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kamil Fnukal
 * ManagerDaoImplTests class is used to verify correct functionality of Manager DAO and Manager Entity.
 */

@ContextConfiguration(classes = PersistenceAirportManagerContext.class)
public class ManagerDaoImplTests extends TestBase<Manager> {

    @Autowired
    private ManagerDao managerDao;

    private Manager managerJohn;
    private Manager managerSteve;

    public ManagerDaoImplTests() { super(Manager.class); }

    @Override
    protected void beforeEach() {
        managerJohn = new Manager("John", "Parkson", "pwd1111", "dump@mail.cz", "1544487489");
        managerSteve = new Manager("Steve", "Peterson", "pws31", "en@mail.cz", "6666666666");

        persistEntities(List.of(managerJohn, managerSteve));
    }

    @Test
    public void testCreateManager() {
        var manager = new Manager("Novy", "manager", "heslo", "emi@mail.cz", "1111111111");
        managerDao.create(manager);

        Assert.assertEquals(findAll().size(), 3);
        var managerFromDb = findById(manager.getId());
        Assert.assertEquals(managerFromDb, manager);
    }

    @Test
    public void testCreateMultipleManagers(){
        var manager1 = new Manager("Artus", "Link", "pwd11357", "emsad@em.cz", "654789123");
        var manager2 = new Manager("Martin", "Kink", "pwd11347", "emsad2@em.cz", "654789125");
        var manager3 = new Manager("Mike", "Jink", "pwd11367", "emsad3@em.cz", "654789128");

        managerDao.create(manager1);
        managerDao.create(manager2);
        managerDao.create(manager3);

        Assert.assertEquals(findAll().size(), 5);
        Assert.assertEquals(findById(manager1.getId()), manager1);
        Assert.assertEquals(findById(manager2.getId()), manager2);
        Assert.assertEquals(findById(manager3.getId()), manager3);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testCreateManagerWithNonUniqueEmail_shouldThrowJpaSystemException() {
        var manager = new Manager("Novy", "manager", "heslo", "en@mail.cz", "1111111111");
        managerDao.create(manager);
    }

    @Test
    public void testFindById() {
        var managerFromDb = managerDao.findById(managerSteve.getId());
        Assert.assertEquals(managerFromDb, managerSteve);
    }

    @Test
    public void testFindByEmail() {
        var managerFromDb = managerDao.findByEmail("dump@mail.cz");
        Assert.assertEquals(managerFromDb, managerJohn);
    }

    @Test
    public void testFindManagerOfFlight() {
        var manager = new Manager("Artus", "Link", "pwd11357", "emsad@em.cz", "654789123");
        var airplane = new Airplane(AirplaneManufacturer.BOEING, 440, "777", "1");
        var origin = new Airport("country", "city", "name", "code");
        var destination = new Airport("country2", "city2", "name2", "code2");
        var flight = new Flight(LocalDateTime.of(2022, 4, 12, 14, 0), LocalDateTime.of(2022, 4, 13, 14, 0), origin, destination, airplane);
        manager.addFlight(flight);

        persistEntities(List.of(airplane, origin, destination, flight, manager));

        var foundManager = managerDao.findManagerOfFlight(flight.getId());
        Assert.assertNotNull(foundManager);
        Assert.assertEquals(foundManager.getFlights().size(), 1);
        Assert.assertEquals(foundManager.getFlights().toArray()[0], flight);
    }

    @Test
    public void testFindManagerOfUnknownFlight_shouldReturnNull() {
        Assert.assertNull(managerDao.findManagerOfFlight(111L));
    }

    @Test
    public void testFindByIdManagerWithFlights() {
        var manager = new Manager("Artus", "Link", "pwd11357", "emsad@em.cz", "654789123");
        var airplane = new Airplane(AirplaneManufacturer.BOEING, 440, "777", "1");
        var origin = new Airport("country", "city", "name", "code");
        var destination = new Airport("country2", "city2", "name2", "code2");
        var flight = new Flight(LocalDateTime.of(2022, 4, 12, 14, 0), LocalDateTime.of(2022, 4, 13, 14, 0), origin, destination, airplane);
        manager.addFlight(flight);
        persistEntities(List.of(airplane, origin, destination, flight, manager));

        var managerFromDb = managerDao.findById(manager.getId());
        Assert.assertNotNull(managerFromDb);
        Assert.assertEquals(managerFromDb.getFlights().size(), 1);
        Assert.assertEquals(managerFromDb.getFlights().toArray()[0], flight);
    }

    @Test
    public void testFindByNonExistingId_shouldReturnNull() {
        Assert.assertNull(managerDao.findById(999L));
    }

    @Test
    public void testFindAll(){
        var allManagers = managerDao.findAll();
        Assert.assertNotNull(allManagers);
        Assert.assertEquals(allManagers.size(), 2);
        Assert.assertTrue(allManagers.contains(managerJohn));
        Assert.assertTrue(allManagers.contains(managerSteve));
    }

    @Test
    public void testUpdateManager() {
        managerSteve.setFirstName("Peter");
        managerSteve.setLastName("Brooks");
        managerDao.update(managerSteve);

        var updatedManager = findById(managerSteve.getId());
        Assert.assertNotNull(updatedManager);
        Assert.assertEquals(updatedManager.getFirstName(), "Peter");
        Assert.assertEquals(updatedManager.getLastName(), "Brooks");
        Assert.assertEquals(updatedManager, managerSteve);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testUpdateNonExistingAdmin_shouldThrowObjectNotFoundException() {
        var manager = new Manager("Adam", "Woods", "pwd11357", "emeeasf@em.cz", "654789123");
        manager.setId(1L);

        manager.setLastName("Stones");
        managerDao.update(manager);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testUpdateAdminWithNonUniqueEmail_shouldThrowJpaSystemException() {
        managerJohn.setEmail(managerSteve.getEmail());
        managerDao.update(managerJohn);
    }

    @Test
    public void testRemoveManager() {
        Assert.assertEquals(findAll().size(), 2);

        managerDao.remove(managerSteve.getId());

        Assert.assertEquals(findAll().size(), 1);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testRemoveNonExistingAdmin_shouldThrowObjectNotFoundException() {
        var manager = new Manager("Kamil", "Admin", "pwd11357", "em55@em.cz", "654789123");
        manager.setId(1L);

        managerDao.remove(manager.getId());
    }

}
