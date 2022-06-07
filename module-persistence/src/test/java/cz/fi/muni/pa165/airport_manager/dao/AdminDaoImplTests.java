package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.PersistenceAirportManagerContext;

import cz.fi.muni.pa165.airport_manager.entity.Admin;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
import cz.fi.muni.pa165.airport_manager.enums.AirplaneManufacturer;
import cz.fi.muni.pa165.airport_manager.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Kamil Fňukal
 * AdminDaoImplTests class is used to verify correct functionality of Admin DAO and Admin Entity.
 */

@ContextConfiguration(classes = PersistenceAirportManagerContext.class)
public class AdminDaoImplTests extends TestBase<Admin> {

    @Autowired
    private AdminDao adminDao;

    private Admin adminJohn;
    private Admin adminSteve;

    public AdminDaoImplTests() { super(Admin.class); }

    @Override
    protected void beforeEach() {
        adminJohn = new Admin("John", "Nikl", "pwd11357", "em@em.cz", "654789123");
        adminSteve = new Admin("Steve", "Admin", "pwd1337","admin@email.com", "666666666");

        persistEntities(List.of(adminJohn, adminSteve));
    }

    @Test
    public void testCreateAdmin() {
        var admin = new Admin("Artus", "Link", "pwd11357", "emaik@em.cz", "654789123");
        adminDao.create(admin);

        Assert.assertEquals(findAll().size(), 3);
        var adminFromDb = findById(admin.getId());
        Assert.assertEquals(adminFromDb, admin);
    }

    @Test
    public void testCreateMultipleAdmins() {
        var admin1= new Admin("Artus", "Link", "pwd11357", "emsad@em.cz", "654789123");
        var admin2 = new Admin("Martin", "Kink", "pwd11347", "emsad2@em.cz", "654789125");
        var admin3 = new Admin("Mike", "Jink", "pwd11367", "emsad3@em.cz", "654789128");

        adminDao.create(admin1);
        adminDao.create(admin2);
        adminDao.create(admin3);

        Assert.assertEquals(findAll().size(), 5);
        Assert.assertEquals(findById(admin1.getId()), admin1);
        Assert.assertEquals(findById(admin2.getId()), admin2);
        Assert.assertEquals(findById(admin3.getId()), admin3);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testCreateAdminWithNonUniqueEmail_shouldThrowJpaSystemException() {
        var admin = new Admin("John", "Nikl", "pwd11357", "em@em.cz", "654789123");
        adminDao.create(admin);
    }

    @Test
    public void testFindById() {
        var adminFromDb = adminDao.findById(adminSteve.getId());
        Assert.assertEquals(adminFromDb, adminSteve);
    }

    @Test
    public void testFindByEmail() {
        var managerFromDb = adminDao.findByEmail("em@em.cz");
        Assert.assertEquals(managerFromDb, adminJohn);
    }

    @Test
    public void testFindByIdAdminWithAirportsAirplanesStewards() {
        var admin = new Admin("John", "Nikl", "pwd11357", "eiuium@em.cz", "654789123");
        var airport = new Airport("country", "city", "name", "code");
        var airplane = new Airplane(AirplaneManufacturer.BOEING, 440, "777", "1");
        var steward = new Steward("John", "Stone", "1");
        admin.addAirport(airport);
        admin.addAirplane(airplane);
        admin.addSteward(steward);
        persistEntities(List.of(airport, airplane, steward, admin));

        var adminFromDb = adminDao.findById(admin.getId());
        Assert.assertNotNull(adminFromDb);
        Assert.assertEquals(adminFromDb.getAirplanes().size(), 1);
        Assert.assertEquals(adminFromDb.getAirplanes().toArray()[0], airplane);
        Assert.assertEquals(adminFromDb.getAirplanes().size(), 1);
        Assert.assertEquals(adminFromDb.getAirports().toArray()[0], airport);
        Assert.assertEquals(adminFromDb.getStewards().size(), 1);
        Assert.assertEquals(adminFromDb.getStewards().toArray()[0], steward);
    }

    @Test
    public void testFindByNonExistingId_shouldReturnNull() {
        Assert.assertNull(adminDao.findById(999L));
    }

    @Test
    public void testFindAll(){
        var allAdmins = adminDao.findAll();
        Assert.assertNotNull(allAdmins);
        Assert.assertEquals(allAdmins.size(), 2);
        Assert.assertTrue(allAdmins.contains(adminJohn));
        Assert.assertTrue(allAdmins.contains(adminSteve));
    }

    @Test
    public void testFindAdminOfAirplane() {
        var admin = new Admin("John", "Nikl", "pwd11357", "eiuium@em.cz", "654789123");
        var airplane = new Airplane(AirplaneManufacturer.BOEING, 440, "777", "1");
        admin.addAirplane(airplane);
        persistEntities(List.of(airplane, admin));

        var foundAdmin = adminDao.findAdminOfAirplane(airplane.getId());
        Assert.assertNotNull(foundAdmin);
        Assert.assertEquals(foundAdmin.getAirplanes().size(), 1);
        Assert.assertEquals(foundAdmin.getAirplanes().toArray()[0], airplane);
    }

    @Test
    public void testFindAdminOfSteward() {
        var admin = new Admin("John", "Nikl", "pwd11357", "eiuium@em.cz", "654789123");
        var stewardJohn = new Steward("John", "Stone", "1");
        admin.addSteward(stewardJohn);
        persistEntities(List.of(stewardJohn, admin));

        var foundAdmin = adminDao.findAdminOfSteward(stewardJohn.getId());
        Assert.assertNotNull(foundAdmin);
        Assert.assertEquals(foundAdmin.getStewards().size(), 1);
        Assert.assertEquals(foundAdmin.getStewards().toArray()[0], stewardJohn);
    }

    @Test
    public void testFindAdminOfAirport() {
        var admin = new Admin("John", "Nikl", "pwd11357", "eiuium@em.cz", "654789123");
        var airport = new Airport("CZ", "BRNO", "LETISTE", "ŽUMPA");
        admin.addAirport(airport);
        persistEntities(List.of(airport, admin));

        var foundAdmin = adminDao.findAdminOfAirport(airport.getId());
        Assert.assertNotNull(foundAdmin);
        Assert.assertEquals(foundAdmin.getAirports().size(), 1);
        Assert.assertEquals(foundAdmin.getAirports().toArray()[0], airport);
    }

    @Test
    public void testFindAdminOfUnknownAirplane_shouldReturnNull() {
        Assert.assertNull(adminDao.findAdminOfAirplane(111L));
    }

    @Test
    public void testFindAdminOfUnknownSteward_shouldReturnNull() {
        Assert.assertNull(adminDao.findAdminOfSteward(111L));
    }

    @Test
    public void testFindAdminOfUnknownAirport_shouldReturnNull() {
        Assert.assertNull(adminDao.findAdminOfAirport(111L));
    }


    @Test
    public void testUpdateAdmin() {
        adminSteve.setFirstName("Peter");
        adminSteve.setLastName("Brooks");
        adminDao.update(adminSteve);

        var updatedAdmin = findById(adminSteve.getId());
        Assert.assertNotNull(updatedAdmin);
        Assert.assertEquals(updatedAdmin.getFirstName(), "Peter");
        Assert.assertEquals(updatedAdmin.getLastName(), "Brooks");
        Assert.assertEquals(updatedAdmin, adminSteve);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testUpdateNonExistingAdmin_shouldThrowObjectNotFoundException() {
        var admin = new Admin("Adam", "Woods", "pwd11357", "emeeasf@em.cz", "654789123");
        admin.setId(1L);

        admin.setLastName("Stones");
        adminDao.update(admin);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testUpdateAdminWithNonUniqueEmail_shouldThrowJpaSystemException() {
        adminJohn.setEmail(adminSteve.getEmail());
        adminDao.update(adminJohn);
    }

    @Test
    public void testRemoveAdmin() {
        adminDao.remove(adminSteve.getId());
        Assert.assertNull(findById(adminSteve.getId()));
        Assert.assertEquals(findAll().size(), 1);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testRemoveNonExistingAdmin_shouldThrowObjectNotFoundException() {
        var admin = new Admin("Kamil", "Admin", "pwd11357", "em55@em.cz", "654789123");
        admin.setId(1L);

        adminDao.remove(admin.getId());
    }

}
