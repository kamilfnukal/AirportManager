package cz.fi.muni.pa165.airport_manager.dao;

import cz.fi.muni.pa165.airport_manager.PersistenceAirportManagerContext;
import cz.fi.muni.pa165.airport_manager.entity.Airplane;
import cz.fi.muni.pa165.airport_manager.entity.Airport;
import cz.fi.muni.pa165.airport_manager.entity.Flight;
import cz.fi.muni.pa165.airport_manager.entity.Steward;
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
 * @author Matej Michalek
 * StewardDaoImplTests class is used to verify correct functionality of Steward DAO and Steward Entity.
 */

@ContextConfiguration(classes = PersistenceAirportManagerContext.class)
public class StewardDaoImplTests extends TestBase<Steward> {

    @Autowired
    private StewardDaoImpl stewardDao;

    private Steward stewardJohn;
    private Steward stewardSteve;

    public StewardDaoImplTests() {
        super(Steward.class);
    }

    @Override
    protected void beforeEach() {
        stewardJohn = new Steward("John", "Stone", "1");
        stewardSteve = new Steward("Steve", "Wonder", "2");

        persistEntities(List.of(stewardJohn, stewardSteve));
    }

    @Test
    public void testCreateSteward() {
        var steward = new Steward("Adam", "Woods", "3");
        stewardDao.create(steward);

        Assert.assertEquals(findAll().size(), 3);
        var stewardFromDb = findById(steward.getId());
        Assert.assertEquals(stewardFromDb, steward);
    }

    @Test
    public void testCreateMultipleStewards() {
        var steward1 = new Steward("Adam", "Woods", "3");
        var steward2 = new Steward("George", "Winston", "4");
        var steward3 = new Steward("David", "Rush", "5");

        stewardDao.create(steward1);
        stewardDao.create(steward2);
        stewardDao.create(steward3);

        Assert.assertEquals(findAll().size(), 5);
        Assert.assertEquals(findById(steward1.getId()), steward1);
        Assert.assertEquals(findById(steward2.getId()), steward2);
        Assert.assertEquals(findById(steward3.getId()), steward3);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testCreateStewardWithNonUniqueCitizenId_shouldThrowJpaSystemException() {
        var steward = new Steward("John", "Stone", stewardJohn.getCitizenId());
        stewardDao.create(steward);
    }

    @Test
    public void testFindById() {
        var stewardFromDb = stewardDao.findById(stewardJohn.getId());
        Assert.assertEquals(stewardFromDb, stewardJohn);
   }

    @Test
    public void testFindByIdStewardWithFlight() {
        var airplane = new Airplane(AirplaneManufacturer.BOEING, 440, "777", "1");
        var origin = new Airport("country", "city", "name", "code");
        var destination = new Airport("country2", "city2", "name2", "code2");
        var flight = new Flight(LocalDateTime.of(2022, 4, 12, 14, 0), LocalDateTime.of(2022, 4, 13, 14, 0), origin, destination, airplane);
        flight.addSteward(stewardJohn);
        persistEntities(List.of(airplane, origin, destination, flight));

        var stewardFromDb = stewardDao.findById(stewardJohn.getId());
        Assert.assertNotNull(stewardFromDb);
        Assert.assertEquals(stewardFromDb.getFlights().size(), 1);
        Assert.assertEquals(stewardFromDb.getFlights().toArray()[0], flight);
    }

    @Test
    public void testFindByNonExistingId_shouldReturnNull() {
        Assert.assertNull(stewardDao.findById(999L));
   }

    @Test
    public void testFindAll(){
        var allStewards = stewardDao.findAll();
        Assert.assertNotNull(allStewards);
        Assert.assertEquals(allStewards.size(), 2);
        Assert.assertTrue(allStewards.contains(stewardJohn));
        Assert.assertTrue(allStewards.contains(stewardSteve));
    }

    @Test
    public void testUpdateSteward() {
        stewardJohn.setFirstName("Johny");
        stewardJohn.setLastName("Stones");
        stewardDao.update(stewardJohn);

        var updatedSteward = findById(stewardJohn.getId());
        Assert.assertNotNull(updatedSteward);
        Assert.assertEquals(updatedSteward.getFirstName(), "Johny");
        Assert.assertEquals(updatedSteward.getLastName(), "Stones");
        Assert.assertEquals(updatedSteward, stewardJohn);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testUpdateNonExistingSteward_shouldThrowObjectNotFoundException() {
        var steward = new Steward("Adam", "Woods", "3");
        steward.setId(1L);

        steward.setLastName("Stones");
        stewardDao.update(steward);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void testUpdateStewardWithNonUniqueCitizenId_shouldThrowJpaSystemException() {
        stewardJohn.setCitizenId(stewardSteve.getCitizenId());
        stewardDao.update(stewardJohn);
    }

    @Test
    public void testRemoveSteward() {
        stewardDao.remove(stewardJohn.getId());
        Assert.assertNull(findById(stewardJohn.getId()));
        Assert.assertEquals(findAll().size(), 1);
    }

    @Test(expectedExceptions = ObjectNotFoundException.class)
    public void testRemoveNonExistingSteward_shouldThrowObjectNotFoundException() {
        var steward = new Steward("Adam", "Woods", "3");
        steward.setId(1L);

        stewardDao.remove(steward.getId());
    }

}
